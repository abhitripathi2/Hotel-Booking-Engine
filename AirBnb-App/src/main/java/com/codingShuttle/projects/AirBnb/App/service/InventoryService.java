package com.codingShuttle.projects.AirBnb.App.service;

import com.codingShuttle.projects.AirBnb.App.dto.HotelDto;
import com.codingShuttle.projects.AirBnb.App.dto.HotelPriceDto;
import com.codingShuttle.projects.AirBnb.App.dto.HotelSearchRequest;
import com.codingShuttle.projects.AirBnb.App.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);

}
