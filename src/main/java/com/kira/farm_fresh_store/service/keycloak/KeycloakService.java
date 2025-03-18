//package com.kira.farm_fresh_store.service.keycloak;
//
//import jakarta.ws.rs.core.Response;
//import org.keycloak.representations.idm.UserRepresentation;
//import org.keycloak.admin.client.Keycloak;
//import org.keycloak.admin.client.KeycloakBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class KeycloakService {
//
//    @Value("${keycloak.server-url}")
//    private String keycloakServerUrl;
//
//    @Value("${keycloak.realm}")
//    private String realm;
//
//    @Value("${idp.client-id}")
//    private String clientId;
//
//    @Value("${keycloak.admin-username}")
//    private String adminUsername;
//
//    @Value("${keycloak.admin-password}")
//    private String adminPassword;
//
//    public String resetPassword(String email) {
//        // Kết nối Keycloak bằng tài khoản Admin
//        Keycloak keycloak = KeycloakBuilder.builder()
//                .serverUrl(keycloakServerUrl)
//                .realm(realm)
//                .clientId(clientId)
//                .username(adminUsername)
//                .password(adminPassword)
//                .build();
//
//        // Tìm người dùng theo email
//        List<UserRepresentation> users = keycloak.realm(realm).users().search(email);
//
//        if (users.isEmpty()) {
//            return "Không tìm thấy người dùng!";
//        }
//
//        UserRepresentation user = users.get(0);
//
//        try {
//            // Gửi email đặt lại mật khẩu
//            keycloak.realm(realm)
//                    .users()
//                    .get(user.getId())
//                    .executeActionsEmail(List.of("UPDATE_PASSWORD"));
//
//            return "Email đặt lại mật khẩu đã được gửi!";
//        } catch (Exception e) {
//            return "Gửi email thất bại: " + e.getMessage();
//        }
//    }
//
//}
