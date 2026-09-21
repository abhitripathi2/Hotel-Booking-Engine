package com.codingShuttle.projects.AirBnb.App.util;

import com.codingShuttle.projects.AirBnb.App.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppUtils {

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
