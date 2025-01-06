package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.dto.EntityConverter;
import com.kira.farm_fresh_store.dto.LoginRequest;
import com.kira.farm_fresh_store.dto.RegisterUserModel;
import com.kira.farm_fresh_store.dto.UserDto;
import com.kira.farm_fresh_store.dto.identity.ClientTokenExchangeParam;
import com.kira.farm_fresh_store.dto.identity.TokenExchangeResponse;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.response.ApiResponse;
import com.kira.farm_fresh_store.service.user.UserService;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final EntityConverter<User, UserDto> entityConverter;

    @PostMapping("/register")
    @Operation(summary = "API đăng kí tài khoản mới")
    public ResponseEntity<ApiResponse<UserDto>> register(@RequestBody @Valid RegisterUserModel registerUserModel) {

        // Thực hiện đăng ký người dùng
        User userRegister = userService.register(registerUserModel);
        UserDto registeredUser = entityConverter.mapEntityToDto(userRegister, UserDto.class);

        // Trả về ApiResponse với thông báo thành công
        return ResponseEntity.status(CREATED)
                .body(new ApiResponse<UserDto>(FeedBackMessage.CREATE_USER_SUCCESS, registeredUser));

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody @Valid LoginRequest loginRequest) {
        String token = userService.login(loginRequest).getAccessToken();
        return ResponseEntity.status(OK)
                .body(new ApiResponse<String>(FeedBackMessage.LOGIN_USER_SUCCESS, token));
    }

}
