package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.dto.order.CartDto;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.exception.UnauthorizedException;
import com.kira.farm_fresh_store.request.order.CreateCartRequest;
import com.kira.farm_fresh_store.response.ApiResponse;
import com.kira.farm_fresh_store.service.cart.ICartService;
import com.kira.farm_fresh_store.utils.AuthenUtil;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/carts")
public class CartController {

    private final ICartService cartService;

    @PostMapping("/create-cart")
    public ResponseEntity<ApiResponse<CartDto>> createCart(
            @RequestBody CreateCartRequest request
    ) {
        try{
            Long userId = AuthenUtil.getProfileId();
            CartDto addCart = cartService.createOrder(request, userId);
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, addCart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (UnauthorizedException e){
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

}
