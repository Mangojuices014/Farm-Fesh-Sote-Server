package com.kira.farm_fresh_store.service.user;

import com.kira.farm_fresh_store.request.user.LoginRequest;
import com.kira.farm_fresh_store.request.user.RegisterUserModel;
import com.kira.farm_fresh_store.dto.user.UserDto;
import com.kira.farm_fresh_store.dto.identity.TokenExchangeResponse;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.request.user.ResetPasswordRequest;
import com.kira.farm_fresh_store.request.user.UpdateUserRequest;

import javax.security.sasl.AuthenticationException;
import java.util.List;

public interface IUserService {

    User register(RegisterUserModel registerUserModel);

    List<UserDto> getAllUsers();


    TokenExchangeResponse login(LoginRequest loginRequest) throws AuthenticationException;

    UserDto getUserById();

    UserDto updateUser(UpdateUserRequest request);

    Boolean deleteUser(Long userId);

    Boolean resetPassword(ResetPasswordRequest newPassword);

}
