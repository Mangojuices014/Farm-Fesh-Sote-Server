//package com.kira.farm_fresh_store.delegates;
//
//import com.kira.farm_fresh_store.email.EmailService;
//import com.kira.farm_fresh_store.entity.order.Order;
//import com.kira.farm_fresh_store.entity.user.User;
//import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
//import com.kira.farm_fresh_store.repository.OrderRepository;
//import com.kira.farm_fresh_store.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.camunda.bpm.engine.delegate.DelegateExecution;
//import org.camunda.bpm.engine.delegate.JavaDelegate;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class DelegateAutoSendMail implements JavaDelegate {
//
//    private final OrderRepository orderRepository;
//
//    private final UserRepository userRepository;
//
//    private final EmailService emailService;
//
//    @Override
//    public void execute(DelegateExecution delegateExecution) throws Exception {
//        String businessKey = (String) delegateExecution.getVariable("businessKey");
//        Order order = orderRepository.findByBusinessKey(businessKey)
//                .orElseThrow( () -> new ResourceNotFoundException("Order not found"));
//        User user = userRepository.findById(order.getUser().getId())
//                .orElseThrow( () -> new ResourceNotFoundException("User not found"));
//        // Chuẩn bị nội dung email
//        String to = user.getEmail();
//        String subject = "Xác nhận đơn hàng #" + order.getId();
//        String body = buildEmailContent(user, order);
//
//        // Gửi email
//        emailService.sendEmail(to, subject, body);
//
//        log.info("Email sent to: " + to);
//    }
//
//    private String buildEmailContent(User user, Order order) {
//        return "Xin chào " + user.getUsername() + ",\n\n"
//                + "Cảm ơn bạn đã đặt hàng tại cửa hàng chúng tôi!\n"
//                + "Thông tin đơn hàng:\n"
//                + "- Mã đơn hàng: " + order.getId() + "\n"
//                + "- Ngày đặt: " + order.getCreatedDate() + "\n"
//                + "- Tổng tiền: " + order.getTotalPrice() + " VNĐ\n"
//                + "\nChúng tôi sẽ sớm liên hệ để giao hàng.\n"
//                + "Trân trọng,\nĐội ngũ hỗ trợ.";
//    }
//}
