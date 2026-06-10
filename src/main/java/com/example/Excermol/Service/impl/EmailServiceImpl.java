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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
@Slf4j
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
        log.info("Fetching all emails");
        List<EmailResponseDto> emails =
                emailMapper.toResponseList(emailRepository.findAll());

        log.info("Retrieved {} emails", emails.size());

        return emails;
    }

    @Override
    public EmailResponseDto getById(Long id) {

        log.info("Fetching email with id: {}", id);

        Email email = emailRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Email not found with id: {}", id);
                    return new EmailNotFoundException("Email tapılmadı: " + id);
                });

        log.info("Email found with id: {}", id);

        return emailMapper.toResponse(email);
    }

    @Override
    public EmailResponseDto createEmail(EmailRequestDto dto) {

        log.info("Creating email with subject: {}", dto.getSubject());
        Email email = emailMapper.toEntity(dto);

        // Sender
        if (dto.getSenderId() != null) {
            email.setSender(userRepository.findById(dto.getSenderId())
                    .orElseThrow(() ->{
                         log.warn("User not found with id: {}", dto.getSenderId());
                        return new RuntimeException("User tapılmadı");
                    }));
        }

        // Company
        if (dto.getCompanyId() != null) {
            email.setCompany(companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> {
                        log.warn("Company not found with id: {}", dto.getCompanyId());
                        return new RuntimeException("Company tapılmadı");
                    }));
        }

        // Campaign
        if (dto.getCampaignId() != null) {
            email.setCampaign(campaignRepository.findById(dto.getCampaignId())
                    .orElseThrow(() -> {
                        log.warn("Campaign not found with id: {}", dto.getCampaignId());
                        return new RuntimeException("Campaign tapılmadı");
                    }));
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
                            .orElseThrow(() -> {
                                log.warn("Person not found with id: {}", recipientId);
                               return new RuntimeException("Person tapılmadı");
                            });
                    try {
                        emailSenderService.sendHtmlEmail(
                                person.getEmail(),
                                dto.getSubject(),
                                dto.getBody()
                        );
                        log.info("Email sent successfully to {}", person.getEmail());

                    } catch (Exception e) {
                        log.error("Failed to send email to {}", person.getEmail(), e);
                        throw new RuntimeException("Email göndərilmədi: " + e.getMessage());
                    }
                }
            }
            // ← BURAYA QƏDƏR
        }

        Email savedEmail = emailRepository.save(email);

        log.info("Email created successfully with id: {}", savedEmail.getId());

        return emailMapper.toResponse(savedEmail);
    }

    @Override
    public EmailResponseDto updateEmail(Long id, EmailRequestDto dto) {
        log.info("Updating email with id: {}", id);
        Email existing = emailRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("Email not found for update. Id: {}", id);

                    return new EmailNotFoundException("Email tapılmadı: " + id);
                });

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


        Email updatedEmail = emailRepository.save(existing);

        log.info("Email updated successfully. Id: {}", id);

        return emailMapper.toResponse(updatedEmail);    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting email with id: {}", id);

        if (!emailRepository.existsById(id)) {
            log.warn("Email not found for deletion. Id: {}", id);
            throw new EmailNotFoundException("Email tapılmadı: " + id);
        }
        emailRepository.deleteById(id);
        log.info("Email deleted successfully. Id: {}", id);

    }

    @Override
    public List<EmailResponseDto> findByStatus(EmailStatus status) {
        log.info("Fetching emails by status: {}", status);

        return emailMapper.toResponseList(
                emailRepository.findByStatusOrderByCreatedAtDesc(status));
    }

    @Override
    public List<EmailResponseDto> findBySender_Id(Long userId) {
        log.info("Fetching emails by sender id: {}", userId);

        return emailMapper.toResponseList(
                emailRepository.findBySender_Id(userId));
    }

    @Override
    public List<EmailResponseDto> findBySubjectContainingIgnoreCase(String keyword) {
        log.info("Searching emails with keyword: {}", keyword);

        return emailMapper.toResponseList(
                emailRepository.findBySubjectContainingIgnoreCase(keyword));
    }

    @Override
    public List<EmailResponseDto> findByReadFalse() {
        log.info("Fetching unread emails");

        return emailMapper.toResponseList(
                emailRepository.findByReadFalseOrderByCreatedAtDesc());
    }

    @Override
    public List<EmailResponseDto> findByCampaign_Id(Long campaignId) {
        log.info("Fetching emails by campaign id: {}", campaignId);

        return emailMapper.toResponseList(
                emailRepository.findByCampaign_Id(campaignId));
    }

    //read true/false
    @Override
    public EmailResponseDto markAsRead(Long id) {
        log.info("Marking email as read. Id: {}", id);
        Email email = emailRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Email not found. Id: {}", id);
                    return new EmailNotFoundException("Email tapılmadı: " + id);
                });
        email.setRead(true);
        Email updatedEmail = emailRepository.save(email);

        log.info("Email marked as read. Id: {}", id);

        return emailMapper.toResponse(updatedEmail);
    }

}
