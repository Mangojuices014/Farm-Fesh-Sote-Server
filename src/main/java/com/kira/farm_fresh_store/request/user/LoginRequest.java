package com.kira.farm_fresh_store.request.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
