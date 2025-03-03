package com.kira.farm_fresh_store.service.cart;

import com.kira.farm_fresh_store.dto.order.CartDto;
import com.kira.farm_fresh_store.request.order.CreateCartRequest;

public interface ICartService {
    CartDto createOrder(CreateCartRequest request, Long userId);
    CartDto getAllCart();
    CartDto getCartByUser(String username);
    String deleteCartById(String cartId);
    String deleteAllCart();
//    CartDto updateCartById(String cartId, UpdateCartRequest request);
}
