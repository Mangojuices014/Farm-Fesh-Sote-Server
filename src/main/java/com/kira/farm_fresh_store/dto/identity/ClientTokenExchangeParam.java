package com.kira.farm_fresh_store.dto.identity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientTokenExchangeParam {
    String grant_type;
    String client_id;
    String client_secret;
    String scope;
    String username;
    String password;
}
