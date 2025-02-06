//package com.kira.farm_fresh_store.exception;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//import ch.qos.logback.core.spi.ErrorCodes;
//import com.kira.farm_fresh_store.response.KeyCloakError;
//import com.kira.farm_fresh_store.utils.FeedBackMessage;
//import com.kira.farm_fresh_store.utils.enums.ErrorCode;
//import org.springframework.stereotype.Component;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import feign.FeignException;
//import lombok.extern.slf4j.Slf4j;
//
//@Component
//@Slf4j
//public class ErrorNormalizer {
//    private final ObjectMapper objectMapper;
//    private final Map<String, ErrorCode> errorCodeMap;
//
//    public ErrorNormalizer() {
//        objectMapper = new ObjectMapper();
//        errorCodeMap = new HashMap<>();
//
//        errorCodeMap.put("User exists with same username", ErrorCode.USER_EXISTED);
//        errorCodeMap.put("User exists with same email", ErrorCode.EMAIL_EXISTED);
//        errorCodeMap.put("User name is missing", ErrorCode.USERNAME_IS_MISSING);
//    }
//
//    public AppException handleKeyCloakException(FeignException exception) {
//        try {
//            log.warn("Cannot complete request");
//            String content = exception.contentUTF8();  // Debug: In nội dung trả về từ Keycloak
//            log.info("Error content: {}", content);
//
//            var response = objectMapper.readValue(content, KeyCloakError.class);
//            if (Objects.nonNull(response.getErrorMessage()) &&
//                    Objects.nonNull(errorCodeMap.get(response.getErrorMessage()))) {
//                return new AppException(errorCodeMap.get(response.getErrorMessage()));
//            }
//        } catch (JsonProcessingException e) {
//            log.error("Cannot deserialize content", e);
//        }
//        return new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
//    }
//
//}
//
//
//
//
