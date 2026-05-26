package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.EmailService;
import com.example.Excermol.entity.Email;
import com.example.Excermol.entity.Person;
import com.example.Excermol.entity.dtos.EmailRequestDto;
import com.example.Excermol.entity.dtos.EmailResponseDto;
import com.example.Excermol.enums.EmailStatus;
import com.example.Excermol.exception.EmailNotFoundException;
import com.example.Excermol.mapper.EmailMapper;
import com.example.Excermol.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;


@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CampaignRepository campaignRepository;
    private final PersonRepository personRepository;
    private final EmailMapper emailMapper;
    private final EmailSenderService emailSenderService;


    public EmailServiceImpl(EmailRepository emailRepository, UserRepository userRepository, CompanyRepository companyRepository, CampaignRepository campaignRepository, PersonRepository personRepository, EmailMapper emailMapper, EmailSenderService emailSenderService) {
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.campaignRepository = campaignRepository;
        this.personRepository = personRepository;
        this.emailMapper = emailMapper;
        this.emailSenderService = emailSenderService;
    }

    @Override
    public List<EmailResponseDto> getAll() {
        return emailMapper.toResponseList(emailRepository.findAll());
    }

    @Override
    public EmailResponseDto getById(Long id) {
        Email email = emailRepository.findById(id)
                .orElseThrow(() -> new EmailNotFoundException("Email tapılmadı: " + id));
        return emailMapper.toResponse(email);
    }

    @Override
    public EmailResponseDto createEmail(EmailRequestDto dto) {
        Email email = emailMapper.toEntity(dto);

        // Sender
        if (dto.getSenderId() != null) {
            email.setSender(userRepository.findById(dto.getSenderId())
                    .orElseThrow(() -> new RuntimeException("User tapılmadı")));
        }

        // Company
        if (dto.getCompanyId() != null) {
            email.setCompany(companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company tapılmadı")));
        }

        // Campaign
        if (dto.getCampaignId() != null) {
            email.setCampaign(campaignRepository.findById(dto.getCampaignId())
                    .orElseThrow(() -> new RuntimeException("Campaign tapılmadı")));
        }

        // Recipients
        if (dto.getRecipientIds() != null && !dto.getRecipientIds().isEmpty()) {
            email.setRecipients(new HashSet<>(personRepository.findAllById(dto.getRecipientIds())));
        }

        // Əgər email göndərilmişdirsə
        if (dto.getStatus() == EmailStatus.SENT) {
            email.setSentAt(LocalDateTime.now());

            // ← BURAYA ƏLAVƏ ET — real email göndər
            if (dto.getRecipientIds() != null) {
                for (Long recipientId : dto.getRecipientIds()) {
                    Person person = personRepository.findById(recipientId)
                            .orElseThrow(() -> new RuntimeException("Person tapılmadı"));
                    try {
                        emailSenderService.sendHtmlEmail(
                                person.getEmail(),
                                dto.getSubject(),
                                dto.getBody()
                        );
                    } catch (Exception e) {
                        throw new RuntimeException("Email göndərilmədi: " + e.getMessage());
                    }
                }
            }
            // ← BURAYA QƏDƏR
        }

        return emailMapper.toResponse(emailRepository.save(email));
    }

    @Override
    public EmailResponseDto updateEmail(Long id, EmailRequestDto dto) {
        Email existing = emailRepository.findById(id)
                .orElseThrow(() -> new EmailNotFoundException("Email tapılmadı: " + id));

        existing.setSubject(dto.getSubject());
        existing.setBody(dto.getBody());
        existing.setStatus(dto.getStatus());
        existing.setSentAt(dto.getSentAt());
        existing.setLabels(dto.getLabels());


        if (dto.getSenderId() != null) {
            existing.setSender(userRepository.findById(dto.getSenderId())
                    .orElseThrow(() -> new RuntimeException("User tapılmadı")));
        }

        if (dto.getCompanyId() != null) {
            existing.setCompany(companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company tapılmadı")));
        }

        if (dto.getCampaignId() != null) {
            existing.setCampaign(campaignRepository.findById(dto.getCampaignId())
                    .orElseThrow(() -> new RuntimeException("Campaign tapılmadı")));
        }

        if (dto.getRecipientIds() != null && !dto.getRecipientIds().isEmpty()) {
            existing.setRecipients(new HashSet<>(personRepository.findAllById(dto.getRecipientIds())));
        }


        return emailMapper.toResponse(emailRepository.save(existing));
    }

    @Override
    public void deleteById(Long id) {
        if (!emailRepository.existsById(id)) {
            throw new EmailNotFoundException("Email tapılmadı: " + id);
        }
        emailRepository.deleteById(id);
    }

    @Override
    public List<EmailResponseDto> findByStatus(EmailStatus status) {
        return emailMapper.toResponseList(
                emailRepository.findByStatusOrderByCreatedAtDesc(status));
    }

    @Override
    public List<EmailResponseDto> findBySender_Id(Long userId) {
        return emailMapper.toResponseList(
                emailRepository.findBySender_Id(userId));
    }

    @Override
    public List<EmailResponseDto> findBySubjectContainingIgnoreCase(String keyword) {
        return emailMapper.toResponseList(
                emailRepository.findBySubjectContainingIgnoreCase(keyword));
    }

    @Override
    public List<EmailResponseDto> findByReadFalse() {
        return emailMapper.toResponseList(
                emailRepository.findByReadFalseOrderByCreatedAtDesc());
    }

    @Override
    public List<EmailResponseDto> findByCampaign_Id(Long campaignId) {
        return emailMapper.toResponseList(
                emailRepository.findByCampaign_Id(campaignId));
    }

    //read true/false
    @Override
    public EmailResponseDto markAsRead(Long id) {
        Email email = emailRepository.findById(id)
                .orElseThrow(() -> new EmailNotFoundException("Email tapılmadı: " + id));
        email.setRead(true);
        return emailMapper.toResponse(emailRepository.save(email));
    }

}
