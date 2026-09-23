package com.codingShuttle.projects.HotelManagement.App.service;

import com.codingShuttle.projects.HotelManagement.App.dto.RoomDto;

import java.util.List;

public interface RoomService {

    RoomDto createNewRoom(Long hotelId, RoomDto roomDto);

    List<RoomDto> getAllRoomsInHotel(Long hotelId);

    RoomDto getRoomById(Long roomId);

    void deleteRoomById(Long roomId);


    RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto);
}
