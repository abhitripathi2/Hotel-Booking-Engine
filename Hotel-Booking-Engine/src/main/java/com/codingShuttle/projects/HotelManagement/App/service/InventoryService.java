package com.codingShuttle.projects.HotelManagement.App.service;

//import com.codingShuttle.projects.AirBnb.App.dto.*;
import com.codingShuttle.projects.HotelManagement.App.dto.HotelPriceDto;
import com.codingShuttle.projects.HotelManagement.App.dto.HotelSearchRequest;
import com.codingShuttle.projects.HotelManagement.App.dto.InventoryDto;
import com.codingShuttle.projects.HotelManagement.App.dto.UpdateInventoryRequestDto;
import com.codingShuttle.projects.HotelManagement.App.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {
    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);
}
