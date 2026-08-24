package com.codingShuttle.projects.AirBnb.App.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter

public class HotelContactInfo {

    private String address;
    private String phoneNumber;
    private String email;
    private String location;
}
