package com.kira.farm_fresh_store.email;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender; // Đảm bảo inject vào đây

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message); // Nếu vẫn lỗi, kiểm tra bước tiếp theo
    }


//    @PostConstruct
//    private void init() {
//        mailSender = createMailSender();
//    }



//    public void sendEmail(String to, String subject, String senderName, String mailContent) throws MessagingException, UnsupportedEncodingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        var messageHelper = new MimeMessageHelper(message);
//        messageHelper.setFrom(DEFAULT_USERNAME, senderName);
//        messageHelper.setTo(to);
//        messageHelper.setSubject(subject);
//        messageHelper.setText(mailContent, true);
//        mailSender.send(message);
//    }
//
//    private JavaMailSender createMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(DEFAULT_HOST);
//        mailSender.setPort(DEFAULT_PORT);
//        mailSender.setUsername(DEFAULT_USERNAME);
//        mailSender.setPassword(DEFAULT_PASSWORD);
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.smtp.auth", true);
//        props.put("mail.smtp.starttls.enable", true);
//        return mailSender;
//    }
}
