package com.codingShuttle.projects.AirBnb.App.dto;

import com.codingShuttle.projects.AirBnb.App.entity.User;
import com.codingShuttle.projects.AirBnb.App.entity.enums.Gender;
import lombok.Data;

@Data
public class GuestDto {
    private Long id;
    private User user;
    private String fullName;
    private Gender gender;
    private Integer age;
}
