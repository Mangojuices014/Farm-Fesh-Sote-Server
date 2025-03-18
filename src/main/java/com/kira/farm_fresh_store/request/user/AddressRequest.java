package com.kira.farm_fresh_store.request.user;

import lombok.Data;

@Data
public class AddressRequest {
    private String city;
    private String state;
    private String country;
    private String zipCode;
}

