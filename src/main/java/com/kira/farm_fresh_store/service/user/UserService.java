package com.kira.farm_fresh_store.service.user;

import com.kira.farm_fresh_store.dto.EntityConverter;
import com.kira.farm_fresh_store.exception.AppException;
import com.kira.farm_fresh_store.request.LoginRequest;
import com.kira.farm_fresh_store.request.RegisterUserModel;
import com.kira.farm_fresh_store.dto.UserDto;
import com.kira.farm_fresh_store.dto.identity.*;
import com.kira.farm_fresh_store.entity.Photo;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.exception.ErrorNormalizer;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.PhotoRepository;
import com.kira.farm_fresh_store.repository.UserRepository;
import com.kira.farm_fresh_store.request.ResetPasswordRequest;
import com.kira.farm_fresh_store.request.user.UpdateRequestParam;
import com.kira.farm_fresh_store.request.user.UpdateUserRequest;
import com.kira.farm_fresh_store.response.keycloak.IdentityClient;
import com.kira.farm_fresh_store.service.googleDrive.GoogleDriveService;
import com.kira.farm_fresh_store.utils.AuthenUtil;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import com.kira.farm_fresh_store.utils.enums.ETypeAccount;
import com.kira.farm_fresh_store.utils.enums.ETypeUser;
import com.kira.farm_fresh_store.utils.enums.ErrorCode;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        try {
            // Thực hiện gọi Token Exchange
            TokenExchangeResponse token = identityClient.exchangeTokenClient(ClientTokenExchangeParam.builder()
                    .grant_type("password")
                    .client_id(keycloakProvider.getClientID())
                    .client_secret(keycloakProvider.getClientSecret())
                    .username(loginRequest.getUsername())
                    .password(loginRequest.getPassword())
                    .scope("openid")
                    .build());
            return token;
        } catch (FeignException.Unauthorized exception) {
            // Xử lý lỗi Unauthorized (401) từ Keycloak
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        } catch (FeignException exception) {
            // Xử lý các lỗi khác từ Feign (ví dụ: lỗi mạng, timeout)
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
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
    public String uploadImage(MultipartFile file, String userId) throws IOException {
        // Tìm người dùng theo userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

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
            userRepository.save(user);

            // Trả về URL ảnh
            return imageUrl;
        }
        return null;
    }

    @Override
    public UserDto getUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        User profile = userRepository.findByUserId(userId).orElseThrow(
                () -> new ResourceNotFoundException(FeedBackMessage.NOT_USER_FOUND));

        return entityConverter.mapEntityToDto(profile, UserDto.class);
    }


    private String extractUserId(ResponseEntity<?> response) {
        String location = response.getHeaders().get("Location").get(0);
        String[] splitedStr = location.split("/");
        return splitedStr[splitedStr.length - 1];
    }

    @Override
    public UserDto updateUser(UpdateUserRequest request) {
        String profileId = AuthenUtil.getProfileId();

        User theUser = userRepository.findByUserId(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_USER_FOUND));
        var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                .grant_type("client_credentials")
                .client_id(keycloakProvider.getClientID())
                .client_secret(keycloakProvider.getClientSecret())
                .scope("openid").build());
        var response = identityClient.updateUser(
                "Bearer " + token.getAccessToken(),
                profileId,
                UpdateRequestParam.builder()
                        .email(request.getEmail())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .build()
        );

        theUser.setFirstName(request.getFirstName());
        theUser.setLastName(request.getLastName());
        theUser.setEmail(request.getEmail());
        userRepository.save(theUser);

        return entityConverter.mapEntityToDto(theUser, UserDto.class);
    }

    @Override
    public Boolean deleteUser(String profileId) {
        try {
            User theUser = userRepository.findById(profileId)
                    .orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_USER_FOUND));
            var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                    .grant_type("client_credentials")
                    .client_id(keycloakProvider.getClientID())
                    .client_secret(keycloakProvider.getClientSecret())
                    .scope("openid").build());
            identityClient.deleteUser(
                    "Bearer " + token.getAccessToken(),
                    theUser.getUserId());
            userRepository.delete(theUser);
            return true;
        } catch (FeignException exception) {
            throw errorNormalizer.handleKeyCloakException(exception);
        }
    }

    @Override
    public Boolean resetPassword(ResetPasswordRequest request) {
        String profileId = AuthenUtil.getProfileId();

        User theUser = userRepository.findByUserId(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_USER_FOUND));
        var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                .grant_type("client_credentials")
                .client_id(keycloakProvider.getClientID())
                .client_secret(keycloakProvider.getClientSecret())
                .scope("openid").build());
        var response = identityClient.resetPassword(
                "Bearer " + token.getAccessToken(),
                profileId,
                Credential.builder()
                        .temporary(false)
                        .type("password")
                        .value(request.getNewPassword()).build());
        return true;
    }
}
