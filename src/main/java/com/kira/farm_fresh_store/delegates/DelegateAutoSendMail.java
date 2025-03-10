package com.kira.farm_fresh_store.delegates;

import com.kira.farm_fresh_store.email.EmailService;
import com.kira.farm_fresh_store.email.EmailTemplateService;
import com.kira.farm_fresh_store.entity.order.Cart;
import com.kira.farm_fresh_store.entity.order.Order;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.CartRepository;
import com.kira.farm_fresh_store.repository.OrderRepository;
import com.kira.farm_fresh_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DelegateAutoSendMail implements JavaDelegate {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final EmailTemplateService emailTemplateService;
    private final CartRepository cartRepository;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String businessKey = (String) delegateExecution.getVariable("businessKey");
        Order order = orderRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
        if (order == null) {
            throw new ResourceNotFoundException("Không tìm thấy đơn hàng");
        }

        User user = userRepository.findById(order.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Chuẩn bị nội dung email HTML
        String to = user.getEmail();
        String subject = "Xác nhận đơn hàng #" + order.getId();
        String htmlBody = emailTemplateService.buildOrderConfirmationEmail(user, order);

        // Gửi email HTML
        emailService.sendEmail(to, subject, htmlBody);
        log.info("Email HTML đã được gửi đến: {}", to);

        // Lấy danh sách cart đã mua
        List<Cart> carts = cartRepository.findByUserAndSelected(user, 1);

        // Xoá tất cả trong 1 query duy nhất
        cartRepository.deleteAll(carts);
        log.info("Deleted {} carts for user: {}", carts.size(), user.getId());
    }
}
