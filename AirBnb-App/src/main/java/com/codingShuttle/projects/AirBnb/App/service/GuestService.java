package com.codingShuttle.projects.AirBnb.App.service;

import com.codingShuttle.projects.AirBnb.App.dto.GuestDto;

import java.util.List;

public interface GuestService {

    List<GuestDto> getAllGuests();

    GuestDto addNewGuest(GuestDto guestDto);

    void updateGuest(Long guestId, GuestDto guestDto);

    void deleteGuest(Long guestId);
}
