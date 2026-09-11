package com.codingShuttle.projects.AirBnb.App.dto;

import com.codingShuttle.projects.AirBnb.App.entity.HotelContactInfo;
import lombok.Data;

@Data
public class HotelDto {

    private long id;
    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;
    private HotelContactInfo contactInfo;
    private boolean isActive;
}
