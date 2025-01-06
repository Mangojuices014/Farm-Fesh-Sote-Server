package com.kira.farm_fresh_store.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.kira.farm_fresh_store.dto.EntityConverter;
import com.kira.farm_fresh_store.dto.LoginRequest;
import com.kira.farm_fresh_store.dto.RegisterUserModel;
import com.kira.farm_fresh_store.dto.UserDto;
import com.kira.farm_fresh_store.dto.identity.*;
import com.kira.farm_fresh_store.entity.Photo;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.exception.ErrorNormalizer;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.PhotoRepository;
import com.kira.farm_fresh_store.repository.UserRepository;
import com.kira.farm_fresh_store.response.ApiResponse;
import com.kira.farm_fresh_store.response.keycloak.IdentityClient;
import com.kira.farm_fresh_store.service.googleDrive.GoogleDriveService;
import com.kira.farm_fresh_store.utils.enums.ETypeAccount;
import com.kira.farm_fresh_store.utils.enums.ETypeUser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.http.auth.AuthenticationException;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final ModelMapper modelMapper;

    private final KeycloakProvider keycloakProvider;

    private final UserRepository userRepository;

    private final IdentityClient identityClient;

    private final EntityConverter<User, UserDto> entityConverter;

    private final PhotoRepository photoRepository;

    private final GoogleDriveService googleDriveService;

    private final ErrorNormalizer errorNormalizer;

    @Override
    public TokenExchangeResponse login(LoginRequest loginRequest) {
            var token = identityClient.exchangeTokenClient(ClientTokenExchangeParam.builder()
                    .grant_type("password")
                    .client_id(keycloakProvider.getClientID())
                    .client_secret(keycloakProvider.getClientSecret())
                    .username(loginRequest.getUsername())
                    .password(loginRequest.getPassword())
                    .scope("openid")
                    .build());
            log.info("TokenInfo {}", token);
            return token;
    }

    @Override
    public User register(RegisterUserModel registerUserModel) {
        try {
            var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                    .grant_type("client_credentials")
                    .client_id(keycloakProvider.getClientID())
                    .client_secret(keycloakProvider.getClientSecret())
                    .scope("openid").build());


            var creationResponse = identityClient.createUser(
                    "Bearer " + token.getAccessToken(),
                    UserCreationParam.builder()
                            .username(registerUserModel.getUserName())
                            .firstName(registerUserModel.getFirstName())
                            .lastName(registerUserModel.getLastName())
                            .email(registerUserModel.getEmail())
                            .enabled(true)
                            .emailVerified(false)
                            .credentials(List.of(Credential.builder()
                                    .type("password")
                                    .temporary(false)
                                    .value(registerUserModel.getPassword())
                                    .build()))
                            .build());

            String userId = extractUserId(creationResponse);

            // Ánh xạ từ RegisterUserModel sang User
            User user = modelMapper.map(registerUserModel, User.class);
            user.setUserId(userId);
            user.setType_account(ETypeAccount.LOCAL);
            user.setTypeUser(ETypeUser.BUYER);

            // Lưu vào cơ sở dữ liệu
            user = userRepository.save(user);
            return user;
        } catch (FeignException exception) {
            throw errorNormalizer.handleKeyCloakException(exception);
        }
    }


    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> entityConverter.mapEntityToDto(user, UserDto.class))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public User uploadImage(MultipartFile file, String userId) throws IOException {
        // Tìm người dùng theo userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Upload ảnh lên Google Drive và nhận URL
        String imageUrl = googleDriveService.uploadImageToDrive(file);

        // Kiểm tra nếu URL hợp lệ
        if (imageUrl != null && !imageUrl.isEmpty() && user != null) {
            // Tạo đối tượng Photo mới và gán URL
            Photo photo = new Photo();
            photo.setUrl(imageUrl);

            // Lưu ảnh vào cơ sở dữ liệu
            photoRepository.save(photo);

            // Gán ảnh cho người dùng
            user.setPhoto(photo);

            // Lưu người dùng với ảnh mới
            return userRepository.save(user);
        }
        return null;
    }


    private String extractUserId(ResponseEntity<?> response) {
        String location = response.getHeaders().get("Location").get(0);
        String[] splitedStr = location.split("/");
        return splitedStr[splitedStr.length - 1];
    }
}
