package com.codingShuttle.projects.AirBnb.App.service;

import com.codingShuttle.projects.AirBnb.App.dto.BookingDto;
import com.codingShuttle.projects.AirBnb.App.dto.BookingRequest;
import com.codingShuttle.projects.AirBnb.App.dto.GuestDto;
import com.stripe.model.Event;

import java.util.List;

public interface BookingService {

    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);

    String initiatePayment(Long bookingId);

    void capturePayments(Event event);
}
