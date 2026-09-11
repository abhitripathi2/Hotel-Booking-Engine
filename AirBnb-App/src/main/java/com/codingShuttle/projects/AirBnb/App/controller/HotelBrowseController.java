package com.codingShuttle.projects.AirBnb.App.controller;

import com.codingShuttle.projects.AirBnb.App.dto.HotelDto;
import com.codingShuttle.projects.AirBnb.App.dto.HotelInfoDto;
import com.codingShuttle.projects.AirBnb.App.dto.HotelPriceDto;
import com.codingShuttle.projects.AirBnb.App.dto.HotelSearchRequest;
import com.codingShuttle.projects.AirBnb.App.service.HotelService;
import com.codingShuttle.projects.AirBnb.App.service.InventoryService;
import jakarta.persistence.SqlResultSetMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;


    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest) {
       var page = inventoryService.searchHotels(hotelSearchRequest);
       return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }

}
