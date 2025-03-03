package com.kira.farm_fresh_store.utils;

import com.kira.farm_fresh_store.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenUtil {
    public static Long getProfileId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            throw new UnauthorizedException("Người dùng chưa đăng nhập.");
        }
        String name = authentication.getName();
        String[] parts = name.split(":");
        return Long.valueOf(parts[2]);
    }
}
