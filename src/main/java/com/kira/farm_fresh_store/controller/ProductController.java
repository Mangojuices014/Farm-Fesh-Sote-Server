package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.dto.product.ProductDto;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.request.product.CreateProductRequest;
import com.kira.farm_fresh_store.request.product.UpdateProductRequest;
import com.kira.farm_fresh_store.response.ApiResponse;
import com.kira.farm_fresh_store.service.product.ProductService;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "/create-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@ModelAttribute CreateProductRequest request,
                                                                 @RequestParam MultipartFile file) {
        try{
            ProductDto productResponse = productService.createProduct(request, file);
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(CREATED)
                    .body(new ApiResponse<>(FeedBackMessage.CREATE_USER_SUCCESS, productResponse));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/get-product/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable String id) {
        try{
            ProductDto productResponse = productService.getProductById(id);
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.CREATE_USER_SUCCESS, productResponse));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/get-all-product")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        try{
            List<ProductDto> productResponse = productService.getAllProducts();
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, productResponse));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<ApiResponse<?>> deleteByID(@PathVariable String id) {
        try{
            productService.deleteProductById(id);
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>("Đã xóa thành công sẩn phẩm", true));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>("Xóa sản phẩm thất bại", false));
        }
    }

    @PutMapping("/update-product/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> deleteByID(
            @PathVariable String id,
            @ModelAttribute UpdateProductRequest request) {
        try{
            ProductDto productResponse = productService.updateProduct(id, request);
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, productResponse));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }

    }

}
