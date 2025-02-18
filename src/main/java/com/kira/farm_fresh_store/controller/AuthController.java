package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.dto.EntityConverter;
import com.kira.farm_fresh_store.dto.identity.TokenExchangeResponse;
import com.kira.farm_fresh_store.request.user.LoginRequest;
import com.kira.farm_fresh_store.request.user.RegisterUserModel;
import com.kira.farm_fresh_store.dto.user.UserDto;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.response.ApiResponse;
import com.kira.farm_fresh_store.service.user.UserService;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final EntityConverter<User, UserDto> entityConverter;

    @PostMapping("/register")
    @Operation(summary = "API đăng kí tài khoản mới")
    public ResponseEntity<ApiResponse<UserDto>> register(
            @RequestBody @Valid RegisterUserModel registerUserModel) {
        try {
            // Thực hiện đăng ký người dùng
            User userRegister = userService.register(registerUserModel);
            UserDto registeredUser = entityConverter.mapEntityToDto(userRegister, UserDto.class);

            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(CREATED)
                    .body(new ApiResponse<>(FeedBackMessage.CREATE_USER_SUCCESS, registeredUser));
        }catch (IllegalArgumentException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi đăng ký", null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenExchangeResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            TokenExchangeResponse tokenResponse = userService.login(loginRequest);
            return ResponseEntity.ok(new ApiResponse<>(FeedBackMessage.LOGIN_SUCCESS, tokenResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi máy chủ", null));
        }
    }
}
