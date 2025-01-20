package com.kira.farm_fresh_store.dto;

import com.kira.farm_fresh_store.utils.enums.ETypeAccount;
import com.kira.farm_fresh_store.utils.enums.ETypeUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {
    private String profileId;
    private String email;
    private String userName;
    private String lastName;
    private String firstName;
    private boolean enabled;
    private ETypeUser typeUser;
    private ETypeAccount type_account;
}
