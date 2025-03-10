//package com.kira.farm_fresh_store.service.keycloak;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class KeycloakService implements IKeycloakService {
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    @Value("${idp.url}")
//    private String authServerUrl;
//
//    @Value("${keycloak.realm}")
//    private String realm;
//
//    @Value("${idp.client-id}")
//    private String clientId;
//
//    @Value("${keycloak.username}")
//    private String adminUsername;
//
//    @Value("${keycloak.password}")
//    private String adminPassword;
//
//    public String getAdminAccessToken() {
//        String url = authServerUrl + "/realms/master/protocol/openid-connect/token";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "password");
//        body.add("client_id", clientId);
//        body.add("username", adminUsername);
//        body.add("password", adminPassword);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
//        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
//
//        return (String) response.getBody().get("access_token");
//    }
//}
