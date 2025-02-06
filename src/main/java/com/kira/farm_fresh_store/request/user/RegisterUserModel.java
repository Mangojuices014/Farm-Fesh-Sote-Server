package com.kira.farm_fresh_store.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RegisterUserModel implements Serializable {

    @NotBlank(message = "email is REQUIRED")
    @NotNull(message = "email is REQUIRED")
    @Size(max = 50, message = "Email limit is 100 characters")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "username is REQUIRED")
    @NotNull(message = "username is REQUIRED")
    @Size(max = 50, message = "Username must be atleast 10 and max 50 characters")
    private String userName;

    @NotBlank(message = "password is REQUIRED")
    @NotNull(message = "password is REQUIRED")
    @Size(max = 50, message = "Password limit is 50 characters")
    private String password;

    @NotBlank(message = "lastName is REQUIRED")
    @NotNull(message = "lastName is REQUIRED")
    @Size(max = 50, message = "lastName limit is 50 characters")
    private String lastName;

    @NotBlank(message = "firstName is REQUIRED")
    @NotNull(message = "firstName is REQUIRED")
    @Size(max = 50, message = "firstName limit is 50 characters")
    private String firstName;


}
