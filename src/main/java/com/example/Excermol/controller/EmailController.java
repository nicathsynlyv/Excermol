package com.example.Excermol.controller;

import com.example.Excermol.Service.EmailService;
import com.example.Excermol.entity.Email;
import com.example.Excermol.enums.EmailStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // ✅ Bütün emaillər
    @GetMapping
    public List<Email> getAllEmails() {
        return emailService.getAllEmails();
    }

    // ✅ Status-a görə emaillər
    @GetMapping("/status/{status}")
    public List<Email> getEmailsByStatus(@PathVariable EmailStatus status) {
        return emailService.getEmailsByStatus(status);
    }

    // ✅ Oxunma statusuna görə emaillər
    @GetMapping("/read/{isRead}")
    public List<Email> getEmailsByReadStatus(@PathVariable boolean isRead) {
        return emailService.getUnreadEmails(isRead);
    }

    // ✅ Göndəriciyə görə emaillər
    @GetMapping("/sender/{sender}")
    public List<Email> getEmailsBySender(@PathVariable String sender) {
        return emailService.getEmailsBySender(sender);
    }

    // ✅ Alıcıya görə emaillər
    @GetMapping("/recipient/{recipient}")
    public List<Email> getEmailsByRecipient(@PathVariable String recipient) {
        return emailService.getEmailsByRecipient(recipient);
    }

    // ✅ Yeni email yaratmaq
    @PostMapping
    public Email createEmail(@RequestBody Email email) {
        return emailService.createEmail(email);
    }

    // ✅ Email update
    @PutMapping("/{id}")
    public Email updateEmail(@PathVariable Long id, @RequestBody Email email) {
        return emailService.updateEmail(id, email);
    }

    // ✅ Email silmək
    @DeleteMapping("/{id}")
    public void deleteEmail(@PathVariable Long id) {
        emailService.deleteEmail(id);
    }
}
