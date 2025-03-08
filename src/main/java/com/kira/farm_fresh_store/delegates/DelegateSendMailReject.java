package com.kira.farm_fresh_store.delegates;

import com.kira.farm_fresh_store.email.EmailService;
import com.kira.farm_fresh_store.email.EmailTemplateService;
import com.kira.farm_fresh_store.entity.order.Order;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.OrderRepository;
import com.kira.farm_fresh_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DelegateSendMailReject implements JavaDelegate {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final EmailTemplateService emailTemplateService;
    private final EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String businessKey = (String) delegateExecution.getVariable("businessKey");

        Order order = orderRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));

        User user = userRepository.findById(order.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Chuẩn bị nội dung email hủy đơn hàng
        String to = user.getEmail();
        String subject = "Thông báo hủy đơn hàng #" + order.getId();
        String htmlBody = emailTemplateService.buildOrderCancellationEmail(user, order);

        // Gửi email HTML
        emailService.sendEmail(to, subject, htmlBody);
        log.info("Email hủy đơn hàng đã được gửi đến: {}", to);
    }

}
