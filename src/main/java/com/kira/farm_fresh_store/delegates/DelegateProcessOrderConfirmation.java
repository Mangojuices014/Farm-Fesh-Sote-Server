package com.kira.farm_fresh_store.delegates;

import com.kira.farm_fresh_store.entity.order.Order;
import com.kira.farm_fresh_store.entity.order.OrderDetail;
import com.kira.farm_fresh_store.entity.product.Product;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.OrderRepository;
import com.kira.farm_fresh_store.repository.ProductRepository;
import com.kira.farm_fresh_store.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DelegateProcessOrderConfirmation implements JavaDelegate {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = (String) execution.getVariable("businessKey");
        Order order = orderRepository.findByBusinessKey(businessKey.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
        List<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            Product product = orderDetail.getProduct();
            product.setQuantityProduct(product.getQuantityProduct()-orderDetail.getQuantity());
            productRepository.save(product);
        }
        order.setStatus(Status.APPROVED);
        orderRepository.save(order);
        execution.setVariable("businessKey", businessKey);
    }
}
