package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.dto.EntityConverter;
import com.kira.farm_fresh_store.dto.identity.TokenExchangeResponse;
import com.kira.farm_fresh_store.dto.user.OtpRequest;
import com.kira.farm_fresh_store.exception.OTPRetryExceptional;
import com.kira.farm_fresh_store.repository.UserRepository;
import com.kira.farm_fresh_store.request.user.LoginRequest;
import com.kira.farm_fresh_store.request.user.RegisterUserModel;
import com.kira.farm_fresh_store.dto.user.UserDto;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.response.ApiResponse;
import com.kira.farm_fresh_store.service.otp.OtpService;
import com.kira.farm_fresh_store.service.user.UserService;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final EntityConverter<User, UserDto> entityConverter;
    private final OtpService otpService;
    private final UserRepository userRepository;
//    private final KeycloakService keycloakService;


    @PostMapping("/register")
    @Operation(summary = "API đăng kí tài khoản mới")
    public ResponseEntity<ApiResponse<UserDto>> register(
            @RequestBody @Valid RegisterUserModel registerUserModel) {
        try {
            // Thực hiện đăng ký người dùng
            User userRegister = userService.register(registerUserModel);
            UserDto registeredUser = entityConverter.mapEntityToDto(userRegister, UserDto.class);
            otpService.sendOtp(registerUserModel.getEmail());
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
        } catch (AuthenticationException | DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<UserDto>> verifyOtp(@RequestBody OtpRequest otpRequest) {
        try {
            User user = userRepository.findUserByEmail(otpRequest.getEmail());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Không tìm thấy người dùng", null));
            }

            boolean isValid = otpService.validateOTP(otpRequest.getEmail(), otpRequest.getOtp());

            if (isValid) {
                user.setEnabled(true);
                userRepository.save(user); // Lưu trạng thái kích hoạt
                // Convert User sang UserDto để trả về
                UserDto userDto = entityConverter.mapEntityToDto(user, UserDto.class);
                return ResponseEntity.ok(new ApiResponse<>("Xác thực thành công!", userDto));
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("OTP không hợp lệ hoặc đã hết hạn", null));

        } catch (OTPRetryExceptional e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<ApiResponse<String>> resendOtp(@RequestBody @Valid OtpRequest request) {
        try {
            otpService.resendOtp(request.getEmail());
            return ResponseEntity.ok(new ApiResponse<>("OTP mới đã được gửi đến email của bạn.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }



//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
//        String response = keycloakService.resetPassword(email);
//        return ResponseEntity.ok(response);
//    }


//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
//        String accessToken = keycloakService.getAdminAccessToken();
//
//        // Lấy danh sách user theo email
//        String userSearchUrl = authServerUrl + "/admin/realms/" + realm + "/users?email=" + email;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<UserRepresentation[]> response = restTemplate.exchange(userSearchUrl, HttpMethod.GET, entity, UserRepresentation[].class);
//
//        if (response.getBody() == null || response.getBody().length == 0) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email không tồn tại!");
//        }
//
//        String userId = response.getBody()[0].getId(); // Lấy ID của user
//
//        // Gửi email reset mật khẩu
//        String resetUrl = authServerUrl + "/admin/realms/" + realm + "/users/" + userId + "/execute-actions-email";
//        HttpEntity<List<String>> resetRequest = new HttpEntity<>(List.of("UPDATE_PASSWORD"), headers);
//        restTemplate.exchange(resetUrl, HttpMethod.PUT, resetRequest, String.class);
//
//        return ResponseEntity.ok("Email đặt lại mật khẩu đã được gửi!");
//    }
}
