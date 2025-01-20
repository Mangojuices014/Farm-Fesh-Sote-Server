package com.kira.farm_fresh_store.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenUtil {
    public static Long getProfileId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        String[] parts = name.split(":");
        Long userId = Long.valueOf(parts[2]);
        return userId;
    }
}
