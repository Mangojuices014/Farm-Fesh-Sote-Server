package com.kira.farm_fresh_store.service.product;

import com.kira.farm_fresh_store.dto.product.ProductDto;
import com.kira.farm_fresh_store.request.product.CreateProductRequest;
import com.kira.farm_fresh_store.request.product.UpdateProductRequest;
import com.kira.farm_fresh_store.utils.enums.ETypeProduct;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
     ProductDto createProduct(CreateProductRequest request, MultipartFile file) throws Exception;
     ProductDto getProductById(String id);
     List<ProductDto> getAllProducts();
     void deleteProductById(String id);
     ProductDto updateProduct(String id, UpdateProductRequest request);
     List<ProductDto> getProductsByType(ETypeProduct type);
}
