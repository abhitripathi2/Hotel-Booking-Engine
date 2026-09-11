package com.codingShuttle.projects.AirBnb.App.service;

import com.codingShuttle.projects.AirBnb.App.dto.HotelDto;
import com.codingShuttle.projects.AirBnb.App.dto.HotelPriceDto;
import com.codingShuttle.projects.AirBnb.App.dto.HotelSearchRequest;
import com.codingShuttle.projects.AirBnb.App.entity.Hotel;
import com.codingShuttle.projects.AirBnb.App.entity.Inventory;
import com.codingShuttle.projects.AirBnb.App.entity.Room;
import com.codingShuttle.projects.AirBnb.App.repository.HotelMinPriceRepository;
import com.codingShuttle.projects.AirBnb.App.repository.InventoryRepository;
import com.codingShuttle.projects.AirBnb.App.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepository hotelMinPriceRepository;

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

}
