package com.kira.farm_fresh_store.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
