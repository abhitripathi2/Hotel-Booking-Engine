package com.codingShuttle.projects.AirBnb.App.service;

import com.codingShuttle.projects.AirBnb.App.entity.User;
import com.codingShuttle.projects.AirBnb.App.exception.ResourceNotFoundException;
import com.codingShuttle.projects.AirBnb.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long user_id) {
        return userRepository.findById(user_id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user_id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}


