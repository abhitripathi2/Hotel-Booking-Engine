package com.codingShuttle.projects.HotelManagement.App.service;

import com.codingShuttle.projects.HotelManagement.App.dto.ProfileUpdateRequestDto;
import com.codingShuttle.projects.HotelManagement.App.dto.UserDto;
import com.codingShuttle.projects.HotelManagement.App.entity.User;
import com.codingShuttle.projects.HotelManagement.App.exception.ResourceNotFoundException;
import com.codingShuttle.projects.HotelManagement.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.codingShuttle.projects.HotelManagement.App.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User getUserById(Long user_id) {
        return userRepository.findById(user_id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user_id));
    }

    @Override
    public void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto) {
        User user = getCurrentUser();

        if(profileUpdateRequestDto.getName() != null) {
            user.setName(profileUpdateRequestDto.getName());
        }
        if(profileUpdateRequestDto.getGender() != null) {
            user.setGender(profileUpdateRequestDto.getGender());
        }
        if(profileUpdateRequestDto.getDateOfBirth() != null) {
            user.setDateOfBirth(profileUpdateRequestDto.getDateOfBirth());
        }

        userRepository.save(user);

    }

    @Override
    public UserDto getMyProfile() {
        User user = getCurrentUser();
        log.info("Getting the profile of User with id: {}", user.getUser_id());
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}


