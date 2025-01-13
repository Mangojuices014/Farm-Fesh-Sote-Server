package com.kira.farm_fresh_store.utils.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION("Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY("Khóa không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME("Tên người dùng phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("Mật khẩu phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED("Chưa đăng nhập", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    EMAIL_EXISTED("Email đã tồn tại, vui lòng chọn email khác", HttpStatus.BAD_REQUEST),
    USER_EXISTED("Tên người dùng đã tồn tại, vui lòng chọn tên khác", HttpStatus.BAD_REQUEST),
    USERNAME_IS_MISSING("Vui lòng nhập tên người dùng", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED("Người dùng không tồn tại", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("Thông tin tài khoản hoặc mật khẩu không chính xác", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_USERNAME("Tên người dùng hoặc mật khẩu không hợp lệ", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
