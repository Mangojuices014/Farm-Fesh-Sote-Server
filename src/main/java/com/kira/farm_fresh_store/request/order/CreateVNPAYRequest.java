package com.kira.farm_fresh_store.request.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVNPAYRequest {
    private Integer amount;
    private String reason;
}
