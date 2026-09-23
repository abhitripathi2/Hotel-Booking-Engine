package com.codingShuttle.projects.HotelManagement.App.controller;

import com.codingShuttle.projects.HotelManagement.App.dto.HotelInfoDto;
import com.codingShuttle.projects.HotelManagement.App.dto.HotelPriceDto;
import com.codingShuttle.projects.HotelManagement.App.dto.HotelSearchRequest;
import com.codingShuttle.projects.HotelManagement.App.service.HotelService;
import com.codingShuttle.projects.HotelManagement.App.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
