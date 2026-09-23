package com.codingShuttle.projects.HotelManagement.App.dto;

import com.codingShuttle.projects.HotelManagement.App.entity.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileUpdateRequestDto {

    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;

}
