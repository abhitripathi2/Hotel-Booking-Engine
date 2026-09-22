package com.codingShuttle.projects.AirBnb.App.dto;

import com.codingShuttle.projects.AirBnb.App.entity.User;
import com.codingShuttle.projects.AirBnb.App.entity.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GuestDto {
    private Long id;
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
}
