package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.dto.EntityConverter;
import com.kira.farm_fresh_store.dto.RegisterUserModel;
import com.kira.farm_fresh_store.dto.UserDto;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.response.ApiResponse;
import com.kira.farm_fresh_store.service.user.UserService;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

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



    @GetMapping("/get-all-users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers(){
        // Lấy tất cả người dùng từ service
        List<UserDto> users = userService.getAllUsers();

        // Trả về danh sách người dùng trong ApiResponse
        return ResponseEntity.status(HttpStatus.OK) // Đổi từ FOUND sang OK (200)
                .body(new ApiResponse<>(FeedBackMessage.USER_FOUND, users));
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> uploadPhoto(
            @RequestPart(name = "fileImg", required = true) MultipartFile file,
            @RequestParam  String profileId)
            throws IOException {
        try{
            User theUser = userService.uploadImage(file, profileId);
            UserDto userDto = entityConverter.mapEntityToDto(theUser, UserDto.class);
            return ResponseEntity.status(OK).body(new ApiResponse(FeedBackMessage.UPLOAD_PHOTO, userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }

    }
}
