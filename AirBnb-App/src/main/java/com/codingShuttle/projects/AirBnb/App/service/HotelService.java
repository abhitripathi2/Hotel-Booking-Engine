package com.codingShuttle.projects.AirBnb.App.service;

import com.codingShuttle.projects.AirBnb.App.dto.HotelDto;
import com.codingShuttle.projects.AirBnb.App.dto.HotelInfoDto;

public interface HotelService {


    HotelDto createHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long hotelId);
    HotelDto updateHotel(Long hotelId, HotelDto hotelDto);
    void deleteHotelById(Long hotelId);

    void activateHotel(Long hotelId);


    HotelInfoDto getHotelInfoById(Long hotelId);
}
