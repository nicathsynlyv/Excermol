package com.example.Excermol.repository;

import com.example.Excermol.entity.Email;
import com.example.Excermol.enums.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    // Status-a görə emailləri gətir
    List<Email> findByStatus(EmailStatus status);

    // Oxunma statusuna görə
    List<Email> findByIsRead(boolean isRead);

    // Göndəriciyə görə
    List<Email> findBySender(String sender);

    // Alıcıya görə (recipients Set<String> olduğuna görə "containing")
    List<Email> findByRecipientsContaining(String recipient);

}
