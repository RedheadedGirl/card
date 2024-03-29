package ru.sbrf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("zevesgod@mail.ru");
        message.setTo("unbreakablehumanofwholeworld@gmail.com");
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}