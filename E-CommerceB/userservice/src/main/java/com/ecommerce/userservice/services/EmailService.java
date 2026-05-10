package com.ecommerce.userservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendResetCode(String toEmail, String resetCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Password Reset Code");
        message.setText(
            "Your password reset code is: " + resetCode + "\n\n" +
            "This code is valid for 10 minutes.\n" +
            "If you did not request a password reset, ignore this email."
        );
        mailSender.send(message);
    }
}