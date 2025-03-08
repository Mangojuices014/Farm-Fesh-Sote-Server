package com.kira.farm_fresh_store.service.cart;

import com.kira.farm_fresh_store.dto.order.CartDto;
import com.kira.farm_fresh_store.request.order.CreateCartRequest;

import java.util.List;

public interface ICartService {
    CartDto createOrder(CreateCartRequest request, Long userId);
    List<CartDto> getCartByUser();
    String deleteCartById(String cartId);
    String deleteAllCart();
//    CartDto updateCartById(String cartId, UpdateCartRequest request);
}
