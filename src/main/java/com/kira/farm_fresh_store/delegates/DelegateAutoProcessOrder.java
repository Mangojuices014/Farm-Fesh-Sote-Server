package com.kira.farm_fresh_store.delegates;

import com.kira.farm_fresh_store.entity.product.Product;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DelegateAutoProcessOrder implements JavaDelegate {

    private final ProductRepository productRepository;

    @Override
    public void execute(DelegateExecution execution){
        // Lấy danh sách productIds từ Camunda
        @SuppressWarnings("unchecked")
        List<String> productIds = (List<String>) execution.getVariable("productIds");

        if (productIds == null || productIds.isEmpty()) {
            execution.setVariable("process", false);
        }

        boolean isValid = true;

        for (String productId : productIds) {
            if (productId == null) {
                isValid = false;
                execution.setVariable("process", isValid);
                break;
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm: " + productId));

            Integer productStock = product.getQuantityProduct();
            Integer orderQuantity = (Integer) execution.getVariable("quantityOrder_" + productId);

            // Kiểm tra thiếu thông tin
            if (orderQuantity == null || productStock == null) {
                isValid = false;
                execution.setVariable("process", isValid);
                break;
            }

            // Kiểm tra số lượng tồn kho
            if (orderQuantity > productStock) {
                isValid = false;
                execution.setVariable("process", isValid);
                break;
            }
        }

        execution.setVariable("process", isValid);
    }
}
