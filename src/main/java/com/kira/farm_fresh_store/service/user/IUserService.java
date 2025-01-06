package com.kira.farm_fresh_store.service.user;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.kira.farm_fresh_store.dto.LoginRequest;
import com.kira.farm_fresh_store.dto.RegisterUserModel;
import com.kira.farm_fresh_store.dto.UserDto;
import com.kira.farm_fresh_store.dto.identity.TokenExchangeResponse;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {

    User register(RegisterUserModel registerUserModel);

    List<UserDto> getAllUsers();

    User uploadImage(MultipartFile file, String userId) throws IOException;

    TokenExchangeResponse login(LoginRequest loginRequest);

}
