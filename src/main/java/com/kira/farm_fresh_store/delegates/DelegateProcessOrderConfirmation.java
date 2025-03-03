//package com.kira.farm_fresh_store.delegates;
//
//import com.kira.farm_fresh_store.entity.order.Order;
//import com.kira.farm_fresh_store.entity.order.OrderDetail;
//import com.kira.farm_fresh_store.entity.product.Product;
//import com.kira.farm_fresh_store.repository.OrderRepository;
//import com.kira.farm_fresh_store.repository.ProductRepository;
//import com.kira.farm_fresh_store.utils.enums.Status;
//import lombok.RequiredArgsConstructor;
//import org.camunda.bpm.engine.delegate.DelegateExecution;
//import org.camunda.bpm.engine.delegate.JavaDelegate;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class DelegateProcessOrderConfirmation implements JavaDelegate {
//
//    private final OrderRepository orderRepository;
//
//
//    private final ProductRepository productRepository;
//
//    @Override
//    public void execute(DelegateExecution delegateExecution) throws Exception {
//        String businessKey =(String) delegateExecution.getVariable("businessKey");
//        Order order = orderRepository.findByBusinessKey(businessKey).get();
//        List<OrderDetail> orderDetails = order.getOrderDetails();
//        for (OrderDetail orderDetail : orderDetails) {
//            Product product = orderDetail.getProduct();
//            product.setQuantityProduct(product.getQuantityProduct()-orderDetail.getQuantityOrder());
//            productRepository.save(product);
//        }
//        order.setStatus(Status.APPROVED);
//        orderRepository.save(order);
//        delegateExecution.setVariable("businessKey", businessKey);
//    }
//}
