package com.example.Excermol.Service;

import com.example.Excermol.entity.Email;
import com.example.Excermol.enums.EmailFolder;
import com.example.Excermol.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    @Autowired
    private EmailRepository emailRepository;

    public List<Email> getAllEmails() {
        return emailRepository.findAll();
    }

    public Email getEmailById(Long id) {
        return emailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Email not found"));
    }

    public Email createEmail(Email email) {
        return emailRepository.save(email);
    }

    public Email updateEmail(Long id, Email emailDetails) {
        Email email = getEmailById(id);
        email.setSubject(emailDetails.getSubject());
        email.setContent(emailDetails.getContent());
        email.setFolder(emailDetails.getFolder());
        email.setLabels(emailDetails.getLabels());
        email.setRead(emailDetails.isRead());
        return emailRepository.save(email);
    }

    public void deleteEmail(Long id) {
        emailRepository.deleteById(id);
    }

    public List<Email> getEmailsByFolder(EmailFolder folder) {
        return emailRepository.findByFolder(folder);
    }

}
