package com.kira.farm_fresh_store.service.product;

import com.kira.farm_fresh_store.dto.product.ProductDto;
import com.kira.farm_fresh_store.entity.product.*;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.*;
import com.kira.farm_fresh_store.request.product.CreateProductRequest;
import com.kira.farm_fresh_store.request.product.UpdateProductRequest;
import com.kira.farm_fresh_store.service.googleDrive.GoogleDriveService;
import com.kira.farm_fresh_store.utils.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
        if (request.getType() == null) {
            throw new IllegalArgumentException("Loại sản phẩm không được để trống!");
        }

        // ✅ **Tạo Product từ request**
        Product product = modelMapper.map(request, Product.class);
        Product lastCart = productRepository.findFirstByOrderByIdDesc();
        if (lastCart == null) {
            product.setId(util.createNewID("PRODUCT"));
        } else {
            product.setId(util.createIDFromLastID("PRODUCT", 7, lastCart.getId()));
        }
        // ✅ **Upload ảnh lên Google Drive**
        String imageUrl = googleDriveService.uploadImageToDrive(file);
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new Exception("Không thể upload ảnh");
        }
        product.setImage(imageUrl);
        product.setSku(util.generateRandomID());
        product.setActive(true);

        switch (request.getType()) {
            case FISH:
                Fish fish = new Fish();
                fish.setOmega3Content(request.getOmega3Content());
                fish.setWaterType(request.getWaterType());
                fish.setProduct(product);
                product.setFish(fish);
                break;
            case MEAT:
                Meat meat = new Meat();
                meat.setMeatType(request.getMeatType());
                meat.setFatPercentage(request.getFatPercentage());
                meat.setProteinContent(request.getProteinContent());
                meat.setProduct(product);
                product.setMeat(meat);
                break;
            case VEGETABLE:
                Vegetable vegetable = new Vegetable();
                vegetable.setVitaminVegetable(request.getVitaminVegetable());
                vegetable.setLeafy(request.getLeafy());
                vegetable.setProduct(product);
                product.setVegetable(vegetable);
                break;
            case FRUIT:
                Fruit fruit = new Fruit();
                fruit.setVitaminFruit(request.getVitaminFruit());
                fruit.setSweetnessLevel(request.getSweetnessLevel());
                fruit.setProduct(product);
                product.setFruit(fruit);
                break;
            default:
                throw new IllegalArgumentException("Loại sản phẩm không hợp lệ: " + request.getType());
        }
        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    @Override
    public ProductDto getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setSku(product.getSku());
        productDto.setType(product.getType());
        productDto.setOrigin(product.getOrigin());
        productDto.setHarvestDate(product.getHarvestDate());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setWeight(product.getWeight());
        productDto.setQuantityProduct(product.getQuantityProduct());
        productDto.setImage(product.getImage());
        productDto.setActive(product.getActive());

        // Kiểm tra null trước khi truy cập thuộc tính của các entity con
        if (product.getFruit() != null) {
            productDto.setSweetnessLevel(product.getFruit().getSweetnessLevel());
            productDto.setVitaminFruit(product.getFruit().getVitaminFruit());
        }

        if (product.getVegetable() != null) {
            productDto.setVitaminVegetable(product.getVegetable().getVitaminVegetable());
            productDto.setLeafy(product.getVegetable().getLeafy());
        }

        if (product.getMeat() != null) {
            productDto.setFatPercentage(product.getMeat().getFatPercentage());
            productDto.setProteinContent(product.getMeat().getProteinContent());
            productDto.setMeatType(product.getMeat().getMeatType());
        }

        if (product.getFish() != null) {
            productDto.setOmega3Content(product.getFish().getOmega3Content());
            productDto.setWaterType(product.getFish().getWaterType());
        }

        return productDto;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<ProductDto> productDtos = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setSku(product.getSku());
            productDto.setType(product.getType());
            productDto.setOrigin(product.getOrigin());
            productDto.setHarvestDate(product.getHarvestDate());
            productDto.setDescription(product.getDescription());
            productDto.setPrice(product.getPrice());
            productDto.setWeight(product.getWeight());
            productDto.setQuantityProduct(product.getQuantityProduct());
            productDto.setImage(product.getImage());
            productDto.setActive(product.getActive());

            // Kiểm tra null trước khi truy cập thuộc tính của các entity con
            if (product.getFruit() != null) {
                productDto.setSweetnessLevel(product.getFruit().getSweetnessLevel());
                productDto.setVitaminFruit(product.getFruit().getVitaminFruit());
            }

            if (product.getVegetable() != null) {
                productDto.setVitaminVegetable(product.getVegetable().getVitaminVegetable());
                productDto.setLeafy(product.getVegetable().getLeafy());
            }

            if (product.getMeat() != null) {
                productDto.setFatPercentage(product.getMeat().getFatPercentage());
                productDto.setProteinContent(product.getMeat().getProteinContent());
                productDto.setMeatType(product.getMeat().getMeatType());
            }

            if (product.getFish() != null) {
                productDto.setOmega3Content(product.getFish().getOmega3Content());
                productDto.setWaterType(product.getFish().getWaterType());
            }
            productDtos.add(productDto);
        }
        return productDtos;
    }

    @Override
    public void deleteProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
        productRepository.delete(product);
    }

    @Override
    public ProductDto updateProduct(String id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setWeight(request.getWeight());
        product.setQuantityProduct(request.getQuantityProduct());
        product.setOrigin(request.getOrigin());
        product.setHarvestDate(request.getHarvestDate());
        product.setShelfLife(request.getShelfLife());
        switch (product.getType()){
            case FRUIT :
                product.getFruit().setSweetnessLevel(request.getSweetnessLevel());
                product.getFruit().setVitaminFruit(request.getVitaminFruit());
                break;
            case VEGETABLE:
                product.getVegetable().setLeafy(request.getLeafy());
                product.getVegetable().setVitaminVegetable(request.getVitaminVegetable());
                break;
            case MEAT:
                product.getMeat().setProteinContent(request.getProteinContent());
                product.getMeat().setMeatType(request.getMeatType());
                product.getMeat().setProteinContent(product.getMeat().getProteinContent());
                break;
            case FISH:
                product.getFish().setOmega3Content(request.getOmega3Content());
                product.getFish().setWaterType(request.getWaterType());
                break;
            default:
                    break;

        }
        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

}
