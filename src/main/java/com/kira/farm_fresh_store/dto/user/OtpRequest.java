package com.kira.farm_fresh_store.dto.user;

import lombok.Data;

@Data
public class OtpRequest {
    private String email;
    private Integer otp;
}
