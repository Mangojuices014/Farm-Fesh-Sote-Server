package com.kira.farm_fresh_store.service.cart;

import com.kira.farm_fresh_store.dto.order.CartDto;
import com.kira.farm_fresh_store.request.order.CreateCartRequest;

import java.util.List;

public interface ICartService {
    CartDto addCart(CreateCartRequest request, Long userId);
    List<CartDto> getCartByUser();
    String deleteCartById(String cartId);
    String deleteAllCart();
//    CartDto updateCartById(String cartId, UpdateCartRequest request);
    String updateSelectCartById(String cartId);
    String updateQualityPlusCartById(String cartId);
    String updateQualityMinusCartById(String cartId);
}

