package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.dto.EntityConverter;
import com.kira.farm_fresh_store.dto.UserDto;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.request.ResetPasswordRequest;
import com.kira.farm_fresh_store.request.user.UpdateRequestParam;
import com.kira.farm_fresh_store.request.user.UpdateUserRequest;
import com.kira.farm_fresh_store.response.ApiResponse;
import com.kira.farm_fresh_store.service.user.UserService;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    private final EntityConverter<User, UserDto> entityConverter;


    @GetMapping("/get-all-users")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        // Lấy tất cả người dùng từ service
        List<UserDto> users = userService.getAllUsers();

        // Trả về danh sách người dùng trong ApiResponse
        return ResponseEntity.status(HttpStatus.OK) // Đổi từ FOUND sang OK (200)
                .body(new ApiResponse<>(FeedBackMessage.USER_FOUND, users));
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadPhoto(
            @RequestPart(name = "fileImg", required = true) MultipartFile file,
            @RequestParam String profileId) throws IOException {
        try {
            // Gọi service để upload ảnh và lấy URL
            String photoUrl = userService.uploadImage(file, profileId);

            // Kiểm tra nếu URL hợp lệ
            if (photoUrl != null && !photoUrl.isEmpty()) {
                return ResponseEntity.status(OK).body(new ApiResponse<String>(FeedBackMessage.UPLOAD_PHOTO, photoUrl));
            } else {
                return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse<String>("Ảnh không hợp lệ", null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse<String>(FeedBackMessage.NOT_USER_FOUND, null));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> getUserById() {
        try {
            // Gọi UserService để lấy thông tin người dùng
            UserDto theUser = userService.getUserById();

            // Trả về kết quả thành công (HTTP 200)
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(FeedBackMessage.USER_FOUND, theUser));
        } catch (ResourceNotFoundException e) {
            // Trả về lỗi khi không tìm thấy người dùng (HTTP 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Không tìm thấy người dùng", null));
        }
    }

    @PutMapping("/update")
    @Operation(summary = "API thay đổi thông tin người dùng")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@RequestBody UpdateUserRequest request) {
        try {
            UserDto theUser = userService.updateUser(request);
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.UPDATE_USER, theUser));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse<>(FeedBackMessage.NOT_USER_FOUND, null));

        }
    }

    @PutMapping("/reset-password")
    @Operation(summary = "API thay đổi thông tin người dùng")
    public ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody ResetPasswordRequest newPassword) {
        try {
            boolean theUser = userService.resetPassword(newPassword);
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.UPDATE_USER, theUser));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse<>(FeedBackMessage.NOT_USER_FOUND, null));
        }
    }

    @DeleteMapping("/delete/{profileId}")
    @Operation(summary = "API thay đổi thông tin người dùng")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable String profileId) {
        try {
            boolean theUser = userService.deleteUser(profileId);
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.UPDATE_USER, theUser));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse<>(FeedBackMessage.NOT_USER_FOUND, null));
        }
    }

}
