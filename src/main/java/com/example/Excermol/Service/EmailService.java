package com.example.Excermol.Service;

import com.example.Excermol.entity.Email;
import com.example.Excermol.enums.EmailStatus;

import java.util.List;

public interface EmailService extends BaseService<Email,Long> {
    // Status-a görə emailləri gətir
    List<Email> findByStatus(EmailStatus status);

    // Oxunma statusuna görə
    List<Email> findByIsRead(boolean isRead);

    // Göndəriciyə görə
    List<Email> findBySender(String sender);

    // Alıcıya görə (recipients Set<String> olduğuna görə "containing")
    List<Email> findByRecipientsContaining(String recipient);


}
