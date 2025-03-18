package com.kira.farm_fresh_store.service.user;

import com.kira.farm_fresh_store.dto.EntityConverter;
import com.kira.farm_fresh_store.request.user.LoginRequest;
import com.kira.farm_fresh_store.request.user.RegisterUserModel;
import com.kira.farm_fresh_store.dto.user.UserDto;
import com.kira.farm_fresh_store.dto.identity.*;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.UserRepository;
import com.kira.farm_fresh_store.request.user.ResetPasswordRequest;
import com.kira.farm_fresh_store.request.user.UpdateUserRequest;
import com.kira.farm_fresh_store.response.keycloak.IdentityClient;
import com.kira.farm_fresh_store.service.otp.OtpService;
import com.kira.farm_fresh_store.utils.AuthenUtil;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UsersResource;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final OtpService otpService;

    private final ModelMapper modelMapper;

    private final KeycloakProvider keycloakProvider;

    private final UserRepository userRepository;

    private final IdentityClient identityClient;

    private final EntityConverter<User, UserDto> entityConverter;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    public TokenExchangeResponse login(LoginRequest loginRequest) throws AuthenticationException {
            User user = userRepository.findUserByUsername(loginRequest.getUsername());

            // Kiểm tra nếu user không tồn tại
            if (user == null) {
                throw new AuthenticationException("Sai tên đăng nhập hoặc mật khẩu.");
            }

            // Kiểm tra nếu tài khoản bị vô hiệu hóa
            if (!user.getEnabled()) {
                throw new DisabledException("Tài khoản chưa được kích hoạt.");
            }

            // Gửi request đến Keycloak để lấy token
            return identityClient.exchangeTokenClient(ClientTokenExchangeParam.builder()
                    .grant_type("password")
                    .client_id(keycloakProvider.getClientID())
                    .client_secret(keycloakProvider.getClientSecret())
                    .username(loginRequest.getUsername())
                    .password(loginRequest.getPassword())
                    .scope("openid")
                    .build());
    }


    @Override
    public User register(RegisterUserModel registerUserModel) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(registerUserModel.getUserName())) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(registerUserModel.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        // Nếu không bị trùng, tiếp tục tạo user
        User user = modelMapper.map(registerUserModel, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        return userRepository.save(user);
    }


    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> entityConverter.mapEntityToDto(user, UserDto.class))
                .collect(java.util.stream.Collectors.toList());
    }


    @Override
    public UserDto getUserById() {
        Long userId = AuthenUtil.getProfileId();

        User profile = userRepository.findById(userId).orElseThrow(
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
        Long userId = AuthenUtil.getProfileId();

        User theUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_USER_FOUND));

        theUser.setFirstName(request.getFirstName());
        theUser.setLastName(request.getLastName());
        theUser.setEmail(request.getEmail());
        userRepository.save(theUser);

        return entityConverter.mapEntityToDto(theUser, UserDto.class);
    }

    @Override
    public Boolean deleteUser(Long userId) {
        User theUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_USER_FOUND));
        userRepository.delete(theUser);
        return true;
    }

    @Override
    public Boolean resetPassword(ResetPasswordRequest request) {
        Long profileId = AuthenUtil.getProfileId();

        User theUser = userRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_USER_FOUND));
        theUser.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        userRepository.save(theUser);
        return true;
    }

    @Override
    public boolean isUserEnabled(String username) {
        return false;
    }

}
