package com.codingShuttle.projects.HotelManagement.App.dto;

import com.codingShuttle.projects.HotelManagement.App.entity.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GuestDto {
    private Long id;
    private String fullName;
    private Gender gender;
    private Integer age;
}
