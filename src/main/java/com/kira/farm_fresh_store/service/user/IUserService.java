package com.kira.farm_fresh_store.service.user;

import com.kira.farm_fresh_store.dto.RegisterUserModel;
import com.kira.farm_fresh_store.dto.UserDto;
import com.kira.farm_fresh_store.entity.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {

    User register(RegisterUserModel registerUserModel);

    List<UserDto> getAllUsers();

    User uploadImage(MultipartFile file, String userId) throws IOException;
}
