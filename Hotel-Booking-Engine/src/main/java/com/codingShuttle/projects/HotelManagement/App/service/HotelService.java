package com.codingShuttle.projects.HotelManagement.App.service;

import com.codingShuttle.projects.HotelManagement.App.dto.HotelDto;
import com.codingShuttle.projects.HotelManagement.App.dto.HotelInfoDto;

import java.util.List;

public interface HotelService {


    HotelDto createHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long hotelId);
    HotelDto updateHotel(Long hotelId, HotelDto hotelDto);
    void deleteHotelById(Long hotelId);

    void activateHotel(Long hotelId);


    HotelInfoDto getHotelInfoById(Long hotelId);

    List<HotelDto> getAllHotels();
}
