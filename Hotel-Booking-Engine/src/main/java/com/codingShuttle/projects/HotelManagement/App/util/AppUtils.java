package com.codingShuttle.projects.HotelManagement.App.util;

import com.codingShuttle.projects.HotelManagement.App.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppUtils {

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
