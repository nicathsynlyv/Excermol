package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.EmailSenderService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailSenderServiceTest Unit Tests")
public class EmailSenderServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailSenderService emailSenderService;

    // =========================
    // SEND SIMPLE EMAIL
    // =========================
    @Test
    void sendSimpleEmail_shouldSendEmail_successfully() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() -> emailSenderService.sendSimpleEmail(
                "test@example.com",
                "Test Subject",
                "Test Body"
        ));

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendSimpleEmail_shouldThrowException_whenMailSenderFails() {
        doThrow(new RuntimeException("SMTP error"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        assertThrows(RuntimeException.class, () -> emailSenderService.sendSimpleEmail(
                "test@example.com",
                "Test Subject",
                "Test Body"
        ));
    }

    @Test
    void sendSimpleEmail_shouldCallSendOnce() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailSenderService.sendSimpleEmail("a@b.com", "Subject", "Body");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    // =========================
    // SEND HTML EMAIL
    // =========================
    @Test
    void sendHtmlEmail_shouldSendEmail_successfully() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> emailSenderService.sendHtmlEmail(
                "test@example.com",
                "Test Subject",
                "<h1>Test HTML Body</h1>"
        ));

        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendHtmlEmail_shouldThrowException_whenMailSenderFails() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("SMTP error"))
                .when(mailSender).send(any(MimeMessage.class));

        assertThrows(RuntimeException.class, () -> emailSenderService.sendHtmlEmail(
                "test@example.com",
                "Test Subject",
                "<h1>Test</h1>"
        ));
    }

    @Test
    void sendHtmlEmail_shouldCallCreateMimeMessageOnce() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailSenderService.sendHtmlEmail("a@b.com", "Subject", "<p>Body</p>");

        verify(mailSender, times(1)).createMimeMessage();
    }
}
