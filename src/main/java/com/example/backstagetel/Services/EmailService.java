package com.example.backstagetel.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender mailSender;


    public void send(String to, String subject, String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("bensalem.ala22@gmail.com"); // l'adresse expéditrice
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        mailSender.send(message);
//}
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            message.setFrom("bensalem.ala22@gmail.com"); // l'adresse expéditrice
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true => HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur d'envoi d'email : " + e.getMessage());
        }
    }


}
