package com.kira.farm_fresh_store.service.product;

import com.kira.farm_fresh_store.dto.product.ProductDto;
import com.kira.farm_fresh_store.request.product.CreateProductRequest;
import com.kira.farm_fresh_store.request.product.UpdateProductRequest;

import java.util.List;

public interface IProductService {
     ProductDto createProduct(CreateProductRequest request);
     ProductDto getProductById(String id);
     List<ProductDto> getAllProducts();
     void deleteProductById(String id);
     ProductDto updateProduct(Long id, UpdateProductRequest request);
}
