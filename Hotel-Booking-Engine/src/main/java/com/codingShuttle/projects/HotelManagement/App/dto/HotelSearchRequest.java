package com.codingShuttle.projects.HotelManagement.App.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HotelSearchRequest {

    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private int roomsCount;

    private int page=0;
    private int size=10;

}
