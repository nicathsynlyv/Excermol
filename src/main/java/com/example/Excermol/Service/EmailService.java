package com.example.Excermol.Service;

import com.example.Excermol.entity.Email;

import com.example.Excermol.enums.EmailStatus;
import com.example.Excermol.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    // Bütün emailləri gətir
    public List<Email> getAllEmails() {
        return emailRepository.findAll();
    }

    // Status-a görə emaillər
    public List<Email> getEmailsByStatus(EmailStatus status) {
        return emailRepository.findByStatus(status);
    }

    // Oxunma statusuna görə
    public List<Email> getUnreadEmails(boolean isRead) {
        return emailRepository.findByIsRead(isRead);
    }

    // Göndəriciyə görə
    public List<Email> getEmailsBySender(String sender) {
        return emailRepository.findBySender(sender);
    }

    // Alıcıya görə
    public List<Email> getEmailsByRecipient(String recipient) {
        return emailRepository.findByRecipientsContaining(recipient);
    }

    // Yeni email yaratmaq
    public Email createEmail(Email email) {
        return emailRepository.save(email);
    }

    // Email update
    public Email updateEmail(Long id, Email updatedEmail) {
        Email email = emailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Email tapılmadı!"));

        email.setSubject(updatedEmail.getSubject());
        email.setBody(updatedEmail.getBody());
        email.setRecipients(updatedEmail.getRecipients());
        email.setStatus(updatedEmail.getStatus());
        email.setRead(updatedEmail.isRead());
        email.setAttachments(updatedEmail.getAttachments());

        return emailRepository.save(email);
    }

    // Email silmək
    public void deleteEmail(Long id) {
        emailRepository.deleteById(id);
    }
}
