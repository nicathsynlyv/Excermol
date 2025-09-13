package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.EmailService;
import com.example.Excermol.entity.Email;
import com.example.Excermol.enums.EmailStatus;
import com.example.Excermol.exception.EmailNotFoundException;
import com.example.Excermol.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;

    // BaseService metodları
    @Override
    public List<Email> getAll() {
        return emailRepository.findAll();
    }

    @Override
    public Optional<Email> getById(Long id) {
        return emailRepository.findById(id);
    }

    @Override
    public Email save(Email email) {
        return emailRepository.save(email);
    }

    @Override
    public void deleteById(Long id) {
        emailRepository.deleteById(id);
    }

    // EmailService spesifik metodları
    @Override
    public List<Email> findByStatus(EmailStatus status) {
        return emailRepository.findByStatus(status);
    }

    @Override
    public List<Email> findByIsRead(boolean isRead) {
        return emailRepository.findByIsRead(isRead);
    }

    @Override
    public List<Email> findBySender(String sender) {
        return emailRepository.findBySender(sender);
    }

    @Override
    public List<Email> findByRecipientsContaining(String recipient) {
        return emailRepository.findByRecipientsContaining(recipient);
    }

    // Email update metodu
    public Email updateEmail(Long id, Email updatedEmail) {
        Email email = emailRepository.findById(id)
                .orElseThrow(() -> new EmailNotFoundException("Email ID = " + id + " tapılmadı!"));

        email.setSubject(updatedEmail.getSubject());
        email.setBody(updatedEmail.getBody());
        email.setRecipients(updatedEmail.getRecipients());
        email.setStatus(updatedEmail.getStatus());
        email.setRead(updatedEmail.isRead());
        email.setAttachments(updatedEmail.getAttachments());
        email.setSentAt(updatedEmail.getSentAt());
        email.setUpdatedAt(updatedEmail.getUpdatedAt());
        email.setCampaign(updatedEmail.getCampaign());
        email.setPerson(updatedEmail.getPerson());
        email.setCompany(updatedEmail.getCompany());

        return emailRepository.save(email);
    }
}
