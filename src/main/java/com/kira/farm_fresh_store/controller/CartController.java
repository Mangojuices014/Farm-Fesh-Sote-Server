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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        try {
            Long userId = AuthenUtil.getProfileId();
            CartDto addCart = cartService.addCart(request, userId);
            return ResponseEntity.status(CREATED)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, addCart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/get-all-cart")
    public ResponseEntity<ApiResponse<List<CartDto>>> getCartByUser() {
        try {
            List<CartDto> cart = cartService.getCartByUser();
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete-cart/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCart(@PathVariable String id) {
        try {
            String deleteCart = cartService.deleteCartById(id);
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, deleteCart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete-all-cart")
    public ResponseEntity<ApiResponse<String>> deleteCartByUser() {
        try {
            String deleteByUser = cartService.deleteAllCart();
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, deleteByUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PutMapping("/select-cart/{cartId}")
    public ResponseEntity<ApiResponse<String>> selectCart(@PathVariable String cartId) {
        try {
            String selectCart = cartService.updateSelectCartById(cartId);
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, selectCart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PutMapping("/quality-plus/{cartId}")
    public ResponseEntity<ApiResponse<String>> qualityPlus(@PathVariable String cartId) {
        try {
            String selectCart = cartService.updateQualityPlusCartById(cartId);
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, selectCart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PutMapping("/quality-minus/{cartId}")
    public ResponseEntity<ApiResponse<String>> qualityMinus(@PathVariable String cartId) {
        try {
            String selectCart = cartService.updateQualityMinusCartById(cartId);
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, selectCart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }
}
