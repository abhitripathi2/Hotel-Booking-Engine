package com.codingShuttle.projects.AirBnb.App.dto;

import com.codingShuttle.projects.AirBnb.App.entity.Guest;
import com.codingShuttle.projects.AirBnb.App.entity.Hotel;
import com.codingShuttle.projects.AirBnb.App.entity.Room;
import com.codingShuttle.projects.AirBnb.App.entity.User;
import com.codingShuttle.projects.AirBnb.App.entity.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDto {
    private Long bookingId;
    private Integer roomsCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookingStatus bookingStatus;
    private Set<GuestDto> guests;
}
