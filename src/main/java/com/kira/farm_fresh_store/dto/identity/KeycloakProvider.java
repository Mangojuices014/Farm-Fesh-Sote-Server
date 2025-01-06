package com.kira.farm_fresh_store.dto.identity;

import lombok.Data;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class KeycloakProvider {

    @NonFinal
    @Value("${idp.url}")
    private String serverURL;

    @NonFinal
    @Value("${keycloak.realm}")
    private String realm;

    @Value("${idp.client-id}")
    @NonFinal
    private String clientID;

    @Value("${idp.client-secret}")
    @NonFinal
    private String clientSecret;

    // Các phương thức khác như getInstance() hoặc refreshToken()

}
