package com.codingShuttle.projects.HotelManagement.App.service;

import com.codingShuttle.projects.HotelManagement.App.dto.ProfileUpdateRequestDto;
import com.codingShuttle.projects.HotelManagement.App.dto.UserDto;
import com.codingShuttle.projects.HotelManagement.App.entity.User;

public interface UserService {

    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();
}
