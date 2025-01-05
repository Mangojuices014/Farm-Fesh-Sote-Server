package com.kira.farm_fresh_store.service.user;

import com.kira.farm_fresh_store.dto.EntityConverter;
import com.kira.farm_fresh_store.dto.RegisterUserModel;
import com.kira.farm_fresh_store.dto.UserDto;
import com.kira.farm_fresh_store.dto.identity.Credential;
import com.kira.farm_fresh_store.dto.identity.TokenExchangeParam;
import com.kira.farm_fresh_store.dto.identity.UserCreationParam;
import com.kira.farm_fresh_store.entity.Photo;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.exception.ErrorNormalizer;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.PhotoRepository;
import com.kira.farm_fresh_store.repository.UserRepository;
import com.kira.farm_fresh_store.response.keycloak.IdentityClient;
import com.kira.farm_fresh_store.service.googleDrive.GoogleDriveService;
import com.kira.farm_fresh_store.utils.enums.ETypeAccount;
import com.kira.farm_fresh_store.utils.enums.ETypeUser;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final ModelMapper modelMapper;

    @Value("${idp.client-id}")
    @NonFinal
    String clientId;

    @Value("${idp.client-secret}")
    @NonFinal
    String clientSecret;

    private final UserRepository userRepository;

    private final IdentityClient identityClient;

    private final EntityConverter<User, UserDto> entityConverter;

    private final PhotoRepository photoRepository;

    private final GoogleDriveService googleDriveService;

    private final ErrorNormalizer errorNormalizer;

    @Override
    public User register(RegisterUserModel registerUserModel) {
        try {
            // Tạo tài khoản trong Keycloak
            var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid").build());

            log.info("TokenInfo {}", token);

            // Tạo người dùng trong Keycloak
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
        if (imageUrl != null && !imageUrl.isEmpty()&&user!=null) {
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
