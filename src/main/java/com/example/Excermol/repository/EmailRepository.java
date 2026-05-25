package com.example.Excermol.repository;

import com.example.Excermol.entity.Email;
import com.example.Excermol.enums.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    // Status-a görə — Inbox, Sent, Spam və s.
    List<Email> findByStatusOrderByCreatedAtDesc(EmailStatus status);

    // Sender-ə görə
    List<Email> findBySender_Id(Long userId);

    // Axtarış — subject-ə görə
    List<Email> findBySubjectContainingIgnoreCase(String keyword);

    // Oxunmamış emailler
    List<Email> findByReadFalseOrderByCreatedAtDesc();

    // Campaign-ə görə
    List<Email> findByCampaign_Id(Long campaignId);

}
