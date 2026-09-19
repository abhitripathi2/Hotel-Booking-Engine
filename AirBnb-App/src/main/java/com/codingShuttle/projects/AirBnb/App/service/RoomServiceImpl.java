package com.codingShuttle.projects.AirBnb.App.service;

import com.codingShuttle.projects.AirBnb.App.dto.RoomDto;
import com.codingShuttle.projects.AirBnb.App.entity.Hotel;
import com.codingShuttle.projects.AirBnb.App.entity.Room;
import com.codingShuttle.projects.AirBnb.App.entity.User;
import com.codingShuttle.projects.AirBnb.App.exception.ResourceNotFoundException;
import com.codingShuttle.projects.AirBnb.App.exception.UnAuthorisedException;
import com.codingShuttle.projects.AirBnb.App.repository.HotelRepository;
import com.codingShuttle.projects.AirBnb.App.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;


    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating new room for hotel: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user do not own the hotel with ID: " +hotelId);
        }

        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        // Created inventory as soon as room is created and Hotel is active. Inventory will be created for the room only if the hotel is active. If the hotel is not active, then inventory will be created when the hotel is activated.
        if (hotel.isActive()) {
            inventoryService.initializeRoomForAYear(room);
        }

        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting All rooms in Hotel with id: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user do not own the hotel with ID: " +hotelId);
        }


        return hotel.getRooms().stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .collect(java.util.stream.Collectors.toList());


    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting the room with id: {}", roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public void deleteRoomById(Long roomId) {
        log.info("Deleting the room with id: {}", roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));
      //

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room.getHotel().getOwner())) {
            throw new UnAuthorisedException("This user do not own the room with ID: " +roomId);
        }


        //TODO: Add logic to delete future inventory items if needed
        inventoryService.deleteAllInventories(room);
        roomRepository.deleteById(roomId);
    }
}
