package com.codingShuttle.projects.AirBnb.App.service;

import com.codingShuttle.projects.AirBnb.App.dto.ProfileUpdateRequestDto;
import com.codingShuttle.projects.AirBnb.App.dto.UserDto;
import com.codingShuttle.projects.AirBnb.App.entity.User;

public interface UserService {

    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();
}
