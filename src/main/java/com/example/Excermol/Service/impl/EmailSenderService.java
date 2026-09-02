package com.example.Excermol.Service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSenderService {

    //Spring Mail tərəfindən verilən interfeysdir və SMTP server vasitəsilə email göndərmək üçün istifadə olunur.
    private final JavaMailSender mailSender;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Sadə email göndər
    // Bu metod sadə, yəni plain-text email göndərmək üçündür.  (to → kimə göndərilir, subject → emailin mövzusu ,body → emailin məzmunu)
    public void sendSimpleEmail(String to, String subject, String body) {
        log.info("Sending email to {}", to);

        SimpleMailMessage message = new SimpleMailMessage();  //SimpleMailMessage sadə text üçündür.
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        log.info("Email sent successfully");
    }

    // HTML email göndər
    public void sendHtmlEmail(String to, String subject, String htmlBody) throws Exception {
        log.info("Sending HTML email to {}", to);

        MimeMessage message = mailSender.createMimeMessage();  //MimeMessage isə daha kompleks email üçün istifadə olunur.
        MimeMessageHelper helper = new MimeMessageHelper(message, true);  //true,multipart dəstəyini aktiv edir.
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // true = HTML,Bu mətn HTML kimi qəbul edilsin.
        mailSender.send(message);
        log.info("HTML email sent successfully");
    }
}
