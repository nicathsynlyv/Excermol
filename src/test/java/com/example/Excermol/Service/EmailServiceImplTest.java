package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.EmailServiceImpl;
import com.example.Excermol.Service.impl.EmailSenderService;
import com.example.Excermol.entity.*;
import com.example.Excermol.entity.dtos.EmailRequestDto;
import com.example.Excermol.entity.dtos.EmailResponseDto;
import com.example.Excermol.enums.EmailStatus;
import com.example.Excermol.exception.EmailNotFoundException;
import com.example.Excermol.mapper.EmailMapper;
import com.example.Excermol.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailServiceImpl Unit Tests")
class EmailServiceImplTest {

    @Mock
    private EmailRepository emailRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private CampaignRepository campaignRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private EmailMapper emailMapper;
    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private EmailServiceImpl emailService;

    private Email email;
    private EmailRequestDto requestDto;
    private EmailResponseDto responseDto;

    @BeforeEach
    void setUp() {
        email = new Email();
        email.setId(1L);
        email.setSubject("Test Subject");

        requestDto = new EmailRequestDto();
        requestDto.setSubject("Test Subject");
        requestDto.setBody("Test Body");
        requestDto.setStatus(EmailStatus.DRAFT);

        responseDto = new EmailResponseDto();
        responseDto.setId(1L);
        responseDto.setSubject("Test Subject");
    }

    // =========================
    // GET ALL
    // =========================
    @Test
    void getAll_shouldReturnAllEmails() {
        when(emailRepository.findAll()).thenReturn(List.of(email));
        when(emailMapper.toResponseList(List.of(email))).thenReturn(List.of(responseDto));

        List<EmailResponseDto> result = emailService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSubject()).isEqualTo("Test Subject");
        verify(emailRepository).findAll();
    }

    @Test
    void getAll_shouldReturnEmptyList_whenNoEmails() {
        when(emailRepository.findAll()).thenReturn(List.of());
        when(emailMapper.toResponseList(List.of())).thenReturn(List.of());

        List<EmailResponseDto> result = emailService.getAll();

        assertThat(result).isEmpty();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getById_shouldReturnEmail_whenExists() {
        when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        EmailResponseDto result = emailService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(emailRepository).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(emailRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> emailService.getById(99L));
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void createEmail_shouldSaveAndReturnEmail_withoutRelations() {
        when(emailMapper.toEntity(requestDto)).thenReturn(email);
        when(emailRepository.save(email)).thenReturn(email);
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        EmailResponseDto result = emailService.createEmail(requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getSubject()).isEqualTo("Test Subject");
        verify(emailRepository).save(email);
    }

    @Test
    void createEmail_shouldSetSender_whenSenderIdProvided() {
        requestDto.setSenderId(10L);
        User sender = new User();
        sender.setId(10L);

        when(emailMapper.toEntity(requestDto)).thenReturn(email);
        when(userRepository.findById(10L)).thenReturn(Optional.of(sender));
        when(emailRepository.save(email)).thenReturn(email);
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        emailService.createEmail(requestDto);

        assertThat(email.getSender()).isEqualTo(sender);
        verify(userRepository).findById(10L);
    }

    @Test
    void createEmail_shouldThrowException_whenSenderNotFound() {
        requestDto.setSenderId(10L);
        when(emailMapper.toEntity(requestDto)).thenReturn(email);
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> emailService.createEmail(requestDto));
        verify(emailRepository, never()).save(any());
    }

    @Test
    void createEmail_shouldSetCompany_whenCompanyIdProvided() {
        requestDto.setCompanyId(20L);
        Company company = new Company();
        company.setId(20L);

        when(emailMapper.toEntity(requestDto)).thenReturn(email);
        when(companyRepository.findById(20L)).thenReturn(Optional.of(company));
        when(emailRepository.save(email)).thenReturn(email);
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        emailService.createEmail(requestDto);

        assertThat(email.getCompany()).isEqualTo(company);
    }

    @Test
    void createEmail_shouldThrowException_whenCompanyNotFound() {
        requestDto.setCompanyId(20L);
        when(emailMapper.toEntity(requestDto)).thenReturn(email);
        when(companyRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> emailService.createEmail(requestDto));
    }

    @Test
    void createEmail_shouldSetCampaign_whenCampaignIdProvided() {
        requestDto.setCampaignId(30L);
        Campaign campaign = new Campaign();
        campaign.setId(30L);

        when(emailMapper.toEntity(requestDto)).thenReturn(email);
        when(campaignRepository.findById(30L)).thenReturn(Optional.of(campaign));
        when(emailRepository.save(email)).thenReturn(email);
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        emailService.createEmail(requestDto);

        assertThat(email.getCampaign()).isEqualTo(campaign);
    }

    @Test
    void createEmail_shouldThrowException_whenCampaignNotFound() {
        requestDto.setCampaignId(30L);
        when(emailMapper.toEntity(requestDto)).thenReturn(email);
        when(campaignRepository.findById(30L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> emailService.createEmail(requestDto));
    }

    @Test
    void createEmail_shouldSetRecipients_whenRecipientIdsProvided() {
        requestDto.setRecipientIds(Set.of(40L, 41L));
        Person p1 = new Person();
        p1.setId(40L);
        Person p2 = new Person();
        p2.setId(41L);

        when(emailMapper.toEntity(requestDto)).thenReturn(email);
        when(personRepository.findAllById(requestDto.getRecipientIds())).thenReturn(List.of(p1, p2));
        when(emailRepository.save(email)).thenReturn(email);
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        emailService.createEmail(requestDto);

        assertThat(email.getRecipients()).containsExactlyInAnyOrder(p1, p2);
    }

    @Test
    void createEmail_shouldNotSetRecipients_whenRecipientIdsEmpty() {
        requestDto.setRecipientIds(Set.of());

        when(emailMapper.toEntity(requestDto)).thenReturn(email);
        when(emailRepository.save(email)).thenReturn(email);
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        emailService.createEmail(requestDto);

        assertThat(email.getRecipients()).isNull();
        verify(personRepository, never()).findAllById(any());
    }

    @Test
    void createEmail_shouldSendEmails_whenStatusIsSent() throws Exception {
        requestDto.setStatus(EmailStatus.SENT);
        requestDto.setRecipientIds(Set.of(40L));
        Person person = new Person();
        person.setId(40L);
        person.setEmail("test@example.com");

        when(emailMapper.toEntity(requestDto)).thenReturn(email);
        when(personRepository.findAllById(requestDto.getRecipientIds())).thenReturn(List.of(person));
        when(personRepository.findById(40L)).thenReturn(Optional.of(person));
        when(emailRepository.save(email)).thenReturn(email);
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        emailService.createEmail(requestDto);

        assertThat(email.getSentAt()).isNotNull();
        verify(emailSenderService).sendHtmlEmail("test@example.com", requestDto.getSubject(), requestDto.getBody());
    }

    @Test
    void createEmail_shouldThrowException_whenRecipientPersonNotFoundDuringSend() throws Exception {
        requestDto.setStatus(EmailStatus.SENT);
        requestDto.setRecipientIds(Set.of(40L));

        when(emailMapper.toEntity(requestDto)).thenReturn(email);
        when(personRepository.findAllById(requestDto.getRecipientIds())).thenReturn(List.of());
        when(personRepository.findById(40L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> emailService.createEmail(requestDto));
        verify(emailRepository, never()).save(any());
    }

    @Test
    void createEmail_shouldThrowException_whenSendingFails() throws Exception {
        requestDto.setStatus(EmailStatus.SENT);
        requestDto.setRecipientIds(Set.of(40L));
        Person person = new Person();
        person.setId(40L);
        person.setEmail("fail@example.com");

        when(emailMapper.toEntity(requestDto)).thenReturn(email);
        when(personRepository.findAllById(requestDto.getRecipientIds())).thenReturn(List.of(person));
        when(personRepository.findById(40L)).thenReturn(Optional.of(person));
        doThrow(new RuntimeException("SMTP error"))
                .when(emailSenderService).sendHtmlEmail(anyString(), anyString(), anyString());

        assertThrows(RuntimeException.class, () -> emailService.createEmail(requestDto));
        verify(emailRepository, never()).save(any());
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void updateEmail_shouldUpdateFieldsAndReturnEmail() {
        requestDto.setSubject("Updated Subject");
        requestDto.setBody("Updated Body");

        when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
        when(emailRepository.save(email)).thenReturn(email);
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        emailService.updateEmail(1L, requestDto);

        assertThat(email.getSubject()).isEqualTo("Updated Subject");
        assertThat(email.getBody()).isEqualTo("Updated Body");
        verify(emailRepository).save(email);
    }

    @Test
    void updateEmail_shouldThrowException_whenNotFound() {
        when(emailRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> emailService.updateEmail(99L, requestDto));
        verify(emailRepository, never()).save(any());
    }

    @Test
    void updateEmail_shouldUpdateSender_whenSenderIdProvided() {
        requestDto.setSenderId(10L);
        User sender = new User();
        sender.setId(10L);

        when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
        when(userRepository.findById(10L)).thenReturn(Optional.of(sender));
        when(emailRepository.save(email)).thenReturn(email);
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        emailService.updateEmail(1L, requestDto);

        assertThat(email.getSender()).isEqualTo(sender);
    }

    @Test
    void updateEmail_shouldThrowException_whenSenderNotFound() {
        requestDto.setSenderId(10L);
        when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> emailService.updateEmail(1L, requestDto));
    }

    @Test
    void updateEmail_shouldUpdateCompany_whenCompanyIdProvided() {
        requestDto.setCompanyId(20L);
        Company company = new Company();
        company.setId(20L);

        when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
        when(companyRepository.findById(20L)).thenReturn(Optional.of(company));
        when(emailRepository.save(email)).thenReturn(email);
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        emailService.updateEmail(1L, requestDto);

        assertThat(email.getCompany()).isEqualTo(company);
    }

    @Test
    void updateEmail_shouldUpdateCampaign_whenCampaignIdProvided() {
        requestDto.setCampaignId(30L);
        Campaign campaign = new Campaign();
        campaign.setId(30L);

        when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
        when(campaignRepository.findById(30L)).thenReturn(Optional.of(campaign));
        when(emailRepository.save(email)).thenReturn(email);
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        emailService.updateEmail(1L, requestDto);

        assertThat(email.getCampaign()).isEqualTo(campaign);
    }

    @Test
    void updateEmail_shouldUpdateRecipients_whenRecipientIdsProvided() {
        requestDto.setRecipientIds(Set.of(40L));
        Person person = new Person();
        person.setId(40L);

        when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
        when(personRepository.findAllById(requestDto.getRecipientIds())).thenReturn(List.of(person));
        when(emailRepository.save(email)).thenReturn(email);
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        emailService.updateEmail(1L, requestDto);

        assertThat(email.getRecipients()).containsExactly(person);
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void deleteById_shouldDeleteEmail_whenExists() {
        when(emailRepository.existsById(1L)).thenReturn(true);

        emailService.deleteById(1L);

        verify(emailRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrowException_whenNotFound() {
        when(emailRepository.existsById(99L)).thenReturn(false);

        assertThrows(EmailNotFoundException.class, () -> emailService.deleteById(99L));
        verify(emailRepository, never()).deleteById(any());
    }

    // =========================
    // FILTERS
    // =========================
    @Test
    void findByStatus_shouldReturnFilteredEmails() {
        when(emailRepository.findByStatusOrderByCreatedAtDesc(EmailStatus.SENT)).thenReturn(List.of(email));
        when(emailMapper.toResponseList(List.of(email))).thenReturn(List.of(responseDto));

        List<EmailResponseDto> result = emailService.findByStatus(EmailStatus.SENT);

        assertThat(result).hasSize(1);
        verify(emailRepository).findByStatusOrderByCreatedAtDesc(EmailStatus.SENT);
    }

    @Test
    void findBySender_Id_shouldReturnFilteredEmails() {
        when(emailRepository.findBySender_Id(5L)).thenReturn(List.of(email));
        when(emailMapper.toResponseList(List.of(email))).thenReturn(List.of(responseDto));

        List<EmailResponseDto> result = emailService.findBySender_Id(5L);

        assertThat(result).hasSize(1);
        verify(emailRepository).findBySender_Id(5L);
    }

    @Test
    void findBySubjectContainingIgnoreCase_shouldReturnMatchingEmails() {
        when(emailRepository.findBySubjectContainingIgnoreCase("test")).thenReturn(List.of(email));
        when(emailMapper.toResponseList(List.of(email))).thenReturn(List.of(responseDto));

        List<EmailResponseDto> result = emailService.findBySubjectContainingIgnoreCase("test");

        assertThat(result).hasSize(1);
        verify(emailRepository).findBySubjectContainingIgnoreCase("test");
    }

    @Test
    void findByReadFalse_shouldReturnUnreadEmails() {
        when(emailRepository.findByReadFalseOrderByCreatedAtDesc()).thenReturn(List.of(email));
        when(emailMapper.toResponseList(List.of(email))).thenReturn(List.of(responseDto));

        List<EmailResponseDto> result = emailService.findByReadFalse();

        assertThat(result).hasSize(1);
        verify(emailRepository).findByReadFalseOrderByCreatedAtDesc();
    }

    @Test
    void findByCampaign_Id_shouldReturnFilteredEmails() {
        when(emailRepository.findByCampaign_Id(7L)).thenReturn(List.of(email));
        when(emailMapper.toResponseList(List.of(email))).thenReturn(List.of(responseDto));

        List<EmailResponseDto> result = emailService.findByCampaign_Id(7L);

        assertThat(result).hasSize(1);
        verify(emailRepository).findByCampaign_Id(7L);
    }

    // =========================
    // MARK AS READ
    // =========================
    @Test
    void markAsRead_shouldSetReadTrueAndReturnEmail() {
        when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
        when(emailRepository.save(email)).thenReturn(email);
        when(emailMapper.toResponse(email)).thenReturn(responseDto);

        emailService.markAsRead(1L);

        assertThat(email.isRead()).isTrue();
        verify(emailRepository).save(email);
    }

    @Test
    void markAsRead_shouldThrowException_whenNotFound() {
        when(emailRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> emailService.markAsRead(99L));
        verify(emailRepository, never()).save(any());
    }
}