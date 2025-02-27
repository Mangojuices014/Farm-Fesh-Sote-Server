package com.kira.farm_fresh_store.service.product;

import com.kira.farm_fresh_store.dto.product.ProductDto;
import com.kira.farm_fresh_store.entity.product.Product;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.ProductRepository;
import com.kira.farm_fresh_store.request.product.CreateProductRequest;
import com.kira.farm_fresh_store.request.product.UpdateProductRequest;
import com.kira.farm_fresh_store.service.googleDrive.GoogleDriveService;
import com.kira.farm_fresh_store.utils.Util;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final GoogleDriveService googleDriveService;

    private final ModelMapper modelMapper;

    private final Util util;

    private final ProductRepository productRepository;

    @Override
    public ProductDto createProduct(CreateProductRequest request, MultipartFile file) throws Exception {
        Product product = modelMapper.map(request, Product.class);
        Product lastPlant = productRepository.findFirstByOrderByIdDesc();
        // Upload ảnh lên Google Drive và nhận URL
        String imageUrl = googleDriveService.uploadImageToDrive(file);
        if (lastPlant == null) {
            product.setId(util.createNewID("PD"));
        } else {
            product.setId(util.createIDFromLastID("PD", 2, lastPlant.getId()));
        }
        if (imageUrl == null && imageUrl.isEmpty()) {
            throw new Exception("Không thể upload ảnh");
        }
        product.setImage(imageUrl);
        product.setSku(util.generateRandomID());
        product.setActive(true);
        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    @Override
    public ProductDto getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tím thấy sản phẩm"));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProductById(String id) {
    }

    @Override
    public ProductDto updateProduct(Long id, UpdateProductRequest request) {
        return null;
    }

}
