package com.example.Excermol.controller;

import com.example.Excermol.Service.EmailService;
import com.example.Excermol.entity.Email;
import com.example.Excermol.enums.EmailFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<Email> getAllEmails() {
        return emailService.getAllEmails();
    }

    @GetMapping("/{id}")
    public Email getEmailById(@PathVariable Long id) {
        return emailService.getEmailById(id);
    }

    @PostMapping
    public Email createEmail(@RequestBody Email email) {
        return emailService.createEmail(email);
    }

    @PutMapping("/{id}")
    public Email updateEmail(@PathVariable Long id, @RequestBody Email email) {
        return emailService.updateEmail(id, email);
    }

    @DeleteMapping("/{id}")
    public void deleteEmail(@PathVariable Long id) {
        emailService.deleteEmail(id);
    }

    @GetMapping("/folder/{folder}")
    public List<Email> getEmailsByFolder(@PathVariable EmailFolder folder) {
        return emailService.getEmailsByFolder(folder);
    }
}
