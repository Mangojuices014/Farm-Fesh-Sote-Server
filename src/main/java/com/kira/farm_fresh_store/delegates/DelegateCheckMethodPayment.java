package com.kira.farm_fresh_store.delegates;

import com.kira.farm_fresh_store.entity.order.Order;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
@Service
@Slf4j
@RequiredArgsConstructor
public class DelegateCheckMethodPayment implements JavaDelegate {

    private final OrderRepository orderRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = (String) execution.getVariable("businessKey");
        Order order = orderRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));

        if (order.getOrderInfo().trim().equals("Thanh toán khi nhận hàng")){
            execution.setVariable("orderInfo", "cod");
        }


        if (order.getOrderInfo().trim().equals("Thanh toán Online")){
            execution.setVariable("orderInfo", "online");
        }

    }
}
