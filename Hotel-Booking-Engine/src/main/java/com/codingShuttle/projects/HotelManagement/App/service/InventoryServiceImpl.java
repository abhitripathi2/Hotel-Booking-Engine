package com.codingShuttle.projects.HotelManagement.App.service;

//import com.codingShuttle.projects.AirBnb.App.dto.*;
import com.codingShuttle.projects.HotelManagement.App.dto.HotelPriceDto;
import com.codingShuttle.projects.HotelManagement.App.dto.HotelSearchRequest;
import com.codingShuttle.projects.HotelManagement.App.dto.InventoryDto;
import com.codingShuttle.projects.HotelManagement.App.dto.UpdateInventoryRequestDto;
import com.codingShuttle.projects.HotelManagement.App.entity.Inventory;
import com.codingShuttle.projects.HotelManagement.App.entity.Room;
import com.codingShuttle.projects.HotelManagement.App.entity.User;
import com.codingShuttle.projects.HotelManagement.App.exception.ResourceNotFoundException;
import com.codingShuttle.projects.HotelManagement.App.repository.HotelMinPriceRepository;
import com.codingShuttle.projects.HotelManagement.App.repository.InventoryRepository;
import com.codingShuttle.projects.HotelManagement.App.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.codingShuttle.projects.HotelManagement.App.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final RoomRepository roomRepository;

    @Override
    public void initializeRoomForAYear(Room room) {
        // Implement the logic to initialize room inventory for a year
        LocalDate today = LocalDate.now();
        LocalDate oneYearLater = today.plusYears(1);
        for (; !today.isAfter(oneYearLater); today = today.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .reservedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE) // Default surge factor
                    .totalCount(room.getTotalCount())
                    .closed(false) // Default to ope
                    .build();
            inventoryRepository.save(inventory);

        }

    }

    @Override
    public void deleteAllInventories(Room room) {
        log.info("Deleting all inventories for room: {}", room.getId());

        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels for {} city from {} to {} for {} rooms",
                hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),
                hotelSearchRequest.getRoomsCount());

        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());
        long dateCount =
                ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(), hotelSearchRequest.getEndDate()) + 1;

        //business logic - 90 days in advance booking
         Page<HotelPriceDto> hotelPage = hotelMinPriceRepository.findHotelsWithAvailableInventory(
                hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),
                hotelSearchRequest.getRoomsCount(),
                dateCount, pageable);

        return hotelPage;

    }

    @Override
    public List<InventoryDto> getAllInventoryByRoom(Long roomId) {
        log.info("Fetching all inventory for room: {}", roomId);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        User user = getCurrentUser();
        if (!user.equals(room.getHotel().getOwner())) {
            throw new AccessDeniedException("You are not authorized to access this room");
        }

        return inventoryRepository.findByRoomOrderByDate(room)
                .stream()
                .map(element -> modelMapper.map(element, InventoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto) {
        log.info("Updating all inventory for room with ID: {} between {} and {}", roomId,
                updateInventoryRequestDto.getStartDate(), updateInventoryRequestDto.getEndDate());

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        User user = getCurrentUser();
        if (!user.equals(room.getHotel().getOwner())) {
            throw new AccessDeniedException("You are not authorized to access this room");
        }

        inventoryRepository.getInventoryAndLockBeforeUpdate(roomId,
                updateInventoryRequestDto.getStartDate(), updateInventoryRequestDto.getEndDate());

        inventoryRepository.updateInventory(
                roomId,
                updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate(),
                updateInventoryRequestDto.getSurgeFactor(),
                updateInventoryRequestDto.isClosed()
        );


    }

}
