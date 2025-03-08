package com.kira.farm_fresh_store.email;

import com.kira.farm_fresh_store.entity.order.Order;
import com.kira.farm_fresh_store.entity.user.User;

public interface EmailTemplateService {
    String buildOrderConfirmationEmail(User user, Order order);
    String buildOrderCancellationEmail(User user, Order order);

}
