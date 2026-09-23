package com.codingShuttle.projects.HotelManagement.App.dto;

import com.codingShuttle.projects.HotelManagement.App.entity.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private String name;
    private String email;
    private Long id;
    private Gender gender;
    private LocalDate dateOfBirth;

}
