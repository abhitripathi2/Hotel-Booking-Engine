package com.codingShuttle.projects.HotelManagement.App.service;

import com.codingShuttle.projects.HotelManagement.App.dto.HotelDto;
import com.codingShuttle.projects.HotelManagement.App.dto.HotelInfoDto;
import com.codingShuttle.projects.HotelManagement.App.dto.RoomDto;
import com.codingShuttle.projects.HotelManagement.App.entity.Hotel;
import com.codingShuttle.projects.HotelManagement.App.entity.Room;
import com.codingShuttle.projects.HotelManagement.App.entity.User;
import com.codingShuttle.projects.HotelManagement.App.exception.ResourceNotFoundException;
import com.codingShuttle.projects.HotelManagement.App.exception.UnAuthorisedException;
import com.codingShuttle.projects.HotelManagement.App.repository.HotelRepository;
import com.codingShuttle.projects.HotelManagement.App.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.codingShuttle.projects.HotelManagement.App.util.AppUtils.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;


    @Override
    public HotelDto createHotel(HotelDto hotelDto) {
        log.info("Creating hotel with details: {}", hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);

        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with ID: {}", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long hotelId) {
        log.info("Fetching hotel with ID: {}", hotelId);
       Hotel hotel =  hotelRepository
               .findById(hotelId)
               .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

       User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       if(!user.equals(hotel.getOwner())) {
           throw new UnAuthorisedException("Hotel do not belong to the current user with ID: " + user.getUser_id());
       }

       return modelMapper.map(hotel, HotelDto.class);

    }

    @Override
    public HotelDto updateHotel(Long hotelId, HotelDto hotelDto) {
        log.info("Updating hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("Hotel do not belong to the current user with ID: " + user.getUser_id());
        }

        modelMapper.map(hotelDto, hotel);
        hotel.setId(hotelId); // Ensure the ID remains the same
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long hotelId) {
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("Hotel do not belong to the current user with ID: " + user.getUser_id());
        }

        for(Room room : hotel.getRooms()){
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());
        }
        hotelRepository.deleteById(hotelId);
        

    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activating hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("Hotel do not belong to the current user with ID: " + user.getUser_id());
        }

        hotel.setActive(true);

        //assuming doing it once
        for(Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }

    }

    //public method to get hotel info by id, including rooms
    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
         List<RoomDto> rooms = hotel.getRooms()
                 .stream()
                .map((element) -> modelMapper.map(element, RoomDto.class))
                .toList();

         return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class), rooms);
    }

    @Override
    public List<HotelDto> getAllHotels() {
        User user = getCurrentUser();
        log.info("Fetching all hotels for the admin user with ID: {}", user.getUser_id());
        List<Hotel> hotels = hotelRepository.findByOwner(user);

        return hotels.stream()
                .map((hotel) -> modelMapper.map(hotel, HotelDto.class))
                .collect(Collectors.toList());
    }
}
