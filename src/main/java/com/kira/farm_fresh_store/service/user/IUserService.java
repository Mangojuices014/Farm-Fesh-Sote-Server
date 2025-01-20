package com.kira.farm_fresh_store.service.user;

import com.kira.farm_fresh_store.request.LoginRequest;
import com.kira.farm_fresh_store.request.RegisterUserModel;
import com.kira.farm_fresh_store.dto.UserDto;
import com.kira.farm_fresh_store.dto.identity.TokenExchangeResponse;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.request.ResetPasswordRequest;
import com.kira.farm_fresh_store.request.user.UpdateUserRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {

    User register(RegisterUserModel registerUserModel);

    List<UserDto> getAllUsers();


    TokenExchangeResponse login(LoginRequest loginRequest);

    UserDto getUserById();

    UserDto updateUser(UpdateUserRequest request);

    Boolean deleteUser(Long userId);

    Boolean resetPassword(ResetPasswordRequest newPassword);

}
