package com.kira.farm_fresh_store.service.VNPay;

import jakarta.servlet.http.HttpServletRequest;

public interface IVnpayService {
    String createOrder(String businessKey, String urlReturn, String ipAddr);

    int orderReturn(HttpServletRequest request);
}
