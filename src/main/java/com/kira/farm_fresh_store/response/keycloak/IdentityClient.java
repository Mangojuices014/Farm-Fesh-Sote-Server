package com.kira.farm_fresh_store.response.keycloak;

import com.kira.farm_fresh_store.dto.identity.*;
import com.kira.farm_fresh_store.request.user.UpdateRequestParam;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "identity-client", url = "${idp.url}")
public interface IdentityClient {
    @PostMapping(
            value = "/realms/Assignment/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeToken(@QueryMap TokenExchangeParam param);

    @PostMapping(
            value = "/realms/Assignment/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeTokenClient(@QueryMap ClientTokenExchangeParam param);

    @PostMapping(value = "/admin/realms/Assignment/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(@RequestHeader("authorization") String token, @RequestBody UserCreationParam param);

    @PutMapping(value = "/admin/realms/Assignment/users/{userId}")
    ResponseEntity<?> updateUser(
            @RequestHeader("authorization") String token,
            @PathVariable String userId,
            @RequestBody UpdateRequestParam param
    );

    @DeleteMapping(value = "/admin/realms/Assignment/users/{userId}")
    ResponseEntity<?> deleteUser(
            @RequestHeader("authorization") String token,
            @PathVariable String userId
    );

    @PutMapping(value = "/admin/realms/Assignment/users/{userId}/reset-password")
    ResponseEntity<?> resetPassword(
            @RequestHeader("authorization") String token,
            @PathVariable String userId,
            @RequestBody Credential request
    );

}
