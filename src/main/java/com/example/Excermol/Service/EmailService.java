package com.example.Excermol.Service;

import com.example.Excermol.entity.dtos.EmailRequestDto;
import com.example.Excermol.entity.dtos.EmailResponseDto;
import com.example.Excermol.enums.EmailStatus;

import java.util.List;
//Bu EmailService interface-dir və Email modulunda biznes əməliyyatlarının müqaviləsini
// (contract) müəyyən edir. Burada əsasən “Email ilə hansı əməliyyatları etmək mümkündür?”
// sualının cavabı verilir.
public interface EmailService {

    List<EmailResponseDto> getAll();

    EmailResponseDto getById(Long id);

    EmailResponseDto createEmail(EmailRequestDto dto);

    EmailResponseDto updateEmail(Long id, EmailRequestDto dto);

    void deleteById(Long id);

    List<EmailResponseDto> findByStatus(EmailStatus status);

    List<EmailResponseDto> findBySender_Id(Long userId);

    List<EmailResponseDto> findBySubjectContainingIgnoreCase(String keyword);

    List<EmailResponseDto> findByReadFalse();

    List<EmailResponseDto> findByCampaign_Id(Long campaignId);

    //email oxundada databasede true olsun
    EmailResponseDto markAsRead(Long id);


}
