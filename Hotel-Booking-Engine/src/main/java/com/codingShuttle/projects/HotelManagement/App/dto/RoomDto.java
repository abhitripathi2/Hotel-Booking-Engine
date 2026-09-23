package com.codingShuttle.projects.HotelManagement.App.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomDto {

    private Long id;
    private String roomType;
    private BigDecimal basePrice;
    private Integer capacity;
    private Integer totalCount;
    private String[] photos;
    private String[] amenities;

}
