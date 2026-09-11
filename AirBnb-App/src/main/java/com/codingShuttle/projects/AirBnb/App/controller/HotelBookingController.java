package com.codingShuttle.projects.AirBnb.App.controller;

import com.codingShuttle.projects.AirBnb.App.dto.BookingDto;
import com.codingShuttle.projects.AirBnb.App.dto.BookingRequest;
import com.codingShuttle.projects.AirBnb.App.dto.GuestDto;
import com.codingShuttle.projects.AirBnb.App.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {

    private final BookingService bookingService;


    @PostMapping("/init")
    public ResponseEntity<BookingDto> initialiseBooking(@RequestBody BookingRequest bookingRequest) {
        // Implement the logic to initialize a booking
        return ResponseEntity.ok(bookingService.initialiseBooking(bookingRequest));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(@PathVariable Long bookingId,
                                                @RequestBody List<GuestDto> guestDtoList) {
        // Implement the logic to add guests to a booking
        return ResponseEntity.ok(bookingService.addGuests(bookingId, guestDtoList));
    }
}
