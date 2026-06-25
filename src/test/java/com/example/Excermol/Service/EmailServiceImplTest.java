package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.EmailSenderService;
import com.example.Excermol.Service.impl.EmailServiceImpl;
import com.example.Excermol.entity.Campaign;
import com.example.Excermol.entity.Company;
import com.example.Excermol.entity.Email;
import com.example.Excermol.entity.Person;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.EmailRequestDto;
import com.example.Excermol.entity.dtos.EmailResponseDto;
import com.example.Excermol.enums.EmailStatus;
import com.example.Excermol.exception.EmailNotFoundException;
import com.example.Excermol.mapper.EmailMapper;
import com.example.Excermol.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("EmailServiceImpl Unit Tests")
@ExtendWith(MockitoExtension.class)
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
    @Test
    void getAll_ShouldReturnEmailList() {

        List<Email> emails = List.of(new Email(), new Email());

        List<EmailResponseDto> response =
                List.of(new EmailResponseDto(), new EmailResponseDto());

        when(emailRepository.findAll()).thenReturn(emails);
        when(emailMapper.toResponseList(emails)).thenReturn(response);

        List<EmailResponseDto> result = emailService.getAll();

        assertEquals(2, result.size());

        verify(emailRepository).findAll();
        verify(emailMapper).toResponseList(emails);
    }

}

    @BeforeEach
    void setUp() {
        email = new Email();
        email.setId(1L);
        email.setSubject("Test Subject");
        email.setBody("Test Body");
        email.setStatus(EmailStatus.DRAFT);
        email.setRead(false);

        responseDto = new EmailResponseDto();
        responseDto.setId(1L);
        responseDto.setSubject("Test Subject");

        requestDto = new EmailRequestDto();
        requestDto.setSubject("Test Subject");
        requestDto.setBody("Test Body");
        requestDto.setStatus(EmailStatus.DRAFT);
    }

    // ─── getAll ──────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("getAll()")
    class GetAll {

        @Test
        @DisplayName("Should return all emails as response DTOs")
        void getAll_returnsAllEmails() {
            List<Email> emails = List.of(email);
            List<EmailResponseDto> expected = List.of(responseDto);

            when(emailRepository.findAll()).thenReturn(emails);
            when(emailMapper.toResponseList(emails)).thenReturn(expected);

            List<EmailResponseDto> result = emailService.getAll();

            assertThat(result).hasSize(1).containsExactly(responseDto);
            verify(emailRepository).findAll();
            verify(emailMapper).toResponseList(emails);
        }

        @Test
        @DisplayName("Should return empty list when no emails exist")
        void getAll_returnsEmptyList_whenNoEmails() {
            when(emailRepository.findAll()).thenReturn(Collections.emptyList());
            when(emailMapper.toResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

            List<EmailResponseDto> result = emailService.getAll();

            assertThat(result).isEmpty();
        }
    }

    // ─── getById ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("getById()")
    class GetById {

        @Test
        @DisplayName("Should return email when found")
        void getById_returnsEmail_whenFound() {
            when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
            when(emailMapper.toResponse(email)).thenReturn(responseDto);

            EmailResponseDto result = emailService.getById(1L);

            assertThat(result).isEqualTo(responseDto);
        }

        @Test
        @DisplayName("Should throw EmailNotFoundException when not found")
        void getById_throwsEmailNotFoundException_whenNotFound() {
            when(emailRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> emailService.getById(99L))
                    .isInstanceOf(EmailNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    // ─── createEmail ─────────────────────────────────────────────────────────

    @Nested
    @DisplayName("createEmail()")
    class CreateEmail {

        @Test
        @DisplayName("Should create a DRAFT email without sending")
        void createEmail_draft_doesNotSendEmail() {
            when(emailMapper.toEntity(requestDto)).thenReturn(email);
            when(emailRepository.save(email)).thenReturn(email);
            when(emailMapper.toResponse(email)).thenReturn(responseDto);

            EmailResponseDto result = emailService.createEmail(requestDto);

            assertThat(result).isEqualTo(responseDto);
            verifyNoInteractions(emailSenderService);
        }

        @Test
        @DisplayName("Should set sender when senderId is provided")
        void createEmail_setsSender_whenSenderIdProvided() {
            requestDto.setSenderId(10L);
            User user = new User();
            user.setId(10L);

            when(emailMapper.toEntity(requestDto)).thenReturn(email);
            when(userRepository.findById(10L)).thenReturn(Optional.of(user));
            when(emailRepository.save(email)).thenReturn(email);
            when(emailMapper.toResponse(email)).thenReturn(responseDto);

            emailService.createEmail(requestDto);

            assertThat(email.getSender()).isEqualTo(user);
        }

        @Test
        @DisplayName("Should throw RuntimeException when sender not found")
        void createEmail_throwsRuntimeException_whenSenderNotFound() {
            requestDto.setSenderId(99L);

            when(emailMapper.toEntity(requestDto)).thenReturn(email);
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> emailService.createEmail(requestDto))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should set company when companyId is provided")
        void createEmail_setsCompany_whenCompanyIdProvided() {
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
        @DisplayName("Should throw RuntimeException when company not found")
        void createEmail_throwsRuntimeException_whenCompanyNotFound() {
            requestDto.setCompanyId(99L);

            when(emailMapper.toEntity(requestDto)).thenReturn(email);
            when(companyRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> emailService.createEmail(requestDto))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should set campaign when campaignId is provided")
        void createEmail_setsCampaign_whenCampaignIdProvided() {
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
        @DisplayName("Should set recipients when recipientIds provided")
        void createEmail_setsRecipients_whenRecipientIdsProvided() {
            Person person = new Person();
            person.setId(5L);
            Set<Long> recipientIds = Set.of(5L);
            requestDto.setRecipientIds(recipientIds);

            when(emailMapper.toEntity(requestDto)).thenReturn(email);
            // Servisin içində findAllById-yə ötürülən dəqiq obyekt tipini yazırıq:
            when(personRepository.findAllById(new ArrayList<>(recipientIds))).thenReturn(List.of(person));
            when(emailRepository.save(email)).thenReturn(email);
            when(emailMapper.toResponse(email)).thenReturn(responseDto);

            emailService.createEmail(requestDto);

            assertThat(email.getRecipients()).containsExactly(person);
        }

        @Test
        @DisplayName("Should send real email and set sentAt when status is SENT")
        void createEmail_sendsEmail_whenStatusIsSent() throws Exception {
            Set<Long> recipientIds = Set.of(5L);
            requestDto.setStatus(EmailStatus.SENT);
            requestDto.setRecipientIds(recipientIds);

            Person person = new Person();
            person.setId(5L);
            person.setEmail("recipient@test.com");

            email.setStatus(EmailStatus.SENT);

            when(emailMapper.toEntity(requestDto)).thenReturn(email);
            // Sərt arqument yoxlaması üçün dəqiq tip:
            when(personRepository.findAllById(new ArrayList<>(recipientIds))).thenReturn(List.of(person));
            when(personRepository.findById(5L)).thenReturn(Optional.of(person));
            when(emailRepository.save(email)).thenReturn(email);
            when(emailMapper.toResponse(email)).thenReturn(responseDto);

            emailService.createEmail(requestDto);

            assertThat(email.getSentAt()).isNotNull();
            verify(emailSenderService).sendHtmlEmail(
                    eq("recipient@test.com"),
                    eq("Test Subject"),
                    eq("Test Body")
            );
        }
        @Test
        @DisplayName("Should throw RuntimeException when emailSenderService fails")
        void createEmail_throwsRuntimeException_whenEmailSendingFails() throws Exception {
            Set<Long> recipientIds = Set.of(5L);
            requestDto.setStatus(EmailStatus.SENT);
            requestDto.setRecipientIds(recipientIds);

            Person person = new Person();
            person.setId(5L);
            person.setEmail("fail@test.com");

            email.setStatus(EmailStatus.SENT);

            when(emailMapper.toEntity(requestDto)).thenReturn(email);
            // Sərt arqument yoxlaması üçün dəqiq tip:
            when(personRepository.findAllById(new ArrayList<>(recipientIds))).thenReturn(List.of(person));
            when(personRepository.findById(5L)).thenReturn(Optional.of(person));
            doThrow(new RuntimeException("SMTP error"))
                    .when(emailSenderService).sendHtmlEmail(anyString(), anyString(), anyString());

            assertThatThrownBy(() -> emailService.createEmail(requestDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Email göndərilmədi");
        }

        @Test
        @DisplayName("Should throw RuntimeException when recipient person not found during SENT flow")
        void createEmail_throwsRuntimeException_whenPersonNotFoundDuringSend() {
            requestDto.setStatus(EmailStatus.SENT);
            requestDto.setRecipientIds(Set.of(99L));

            email.setStatus(EmailStatus.SENT);

            when(emailMapper.toEntity(requestDto)).thenReturn(email);
            when(personRepository.findAllById(List.of(99L))).thenReturn(Collections.emptyList());
            when(personRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> emailService.createEmail(requestDto))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    // ─── updateEmail ─────────────────────────────────────────────────────────

    @Nested
    @DisplayName("updateEmail()")
    class UpdateEmail {

        @Test
        @DisplayName("Should update and return email when found")
        void updateEmail_updatesAndReturns_whenFound() {
            requestDto.setSubject("Updated Subject");
            requestDto.setBody("Updated Body");
            requestDto.setStatus(EmailStatus.SENT);
            requestDto.setSentAt(LocalDateTime.now());
            requestDto.setLabels(List.of("important"));

            when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
            when(emailRepository.save(email)).thenReturn(email);
            when(emailMapper.toResponse(email)).thenReturn(responseDto);

            EmailResponseDto result = emailService.updateEmail(1L, requestDto);

            assertThat(result).isEqualTo(responseDto);
            assertThat(email.getSubject()).isEqualTo("Updated Subject");
            assertThat(email.getBody()).isEqualTo("Updated Body");
        }

        @Test
        @DisplayName("Should throw EmailNotFoundException when email not found")
        void updateEmail_throwsEmailNotFoundException_whenNotFound() {
            when(emailRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> emailService.updateEmail(99L, requestDto))
                    .isInstanceOf(EmailNotFoundException.class)
                    .hasMessageContaining("99");
        }

        @Test
        @DisplayName("Should update sender when senderId is provided")
        void updateEmail_updatesSender_whenSenderIdProvided() {
            requestDto.setSenderId(10L);
            User user = new User();
            user.setId(10L);

            when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
            when(userRepository.findById(10L)).thenReturn(Optional.of(user));
            when(emailRepository.save(email)).thenReturn(email);
            when(emailMapper.toResponse(email)).thenReturn(responseDto);

            emailService.updateEmail(1L, requestDto);

            assertThat(email.getSender()).isEqualTo(user);
        }

        @Test
        @DisplayName("Should update company when companyId is provided")
        void updateEmail_updatesCompany_whenCompanyIdProvided() {
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
        @DisplayName("Should update campaign when campaignId is provided")
        void updateEmail_updatesCampaign_whenCampaignIdProvided() {
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
        @DisplayName("Should update recipients when recipientIds provided")
        void updateEmail_updatesRecipients_whenRecipientIdsProvided() {
            Person person = new Person();
            person.setId(5L);
            Set<Long> recipientIds = Set.of(5L);
            requestDto.setRecipientIds(recipientIds);

            when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
            // Sərt arqument yoxlaması üçün dəqiq tip:
            when(personRepository.findAllById(new ArrayList<>(recipientIds))).thenReturn(List.of(person));
            when(emailRepository.save(email)).thenReturn(email);
            when(emailMapper.toResponse(email)).thenReturn(responseDto);

            emailService.updateEmail(1L, requestDto);

            assertThat(email.getRecipients()).containsExactly(person);
        }

        // ─── deleteById ──────────────────────────────────────────────────────────

        @Nested
        @DisplayName("deleteById()")
        class DeleteById {

            @Test
            @DisplayName("Should delete email when it exists")
            void deleteById_deletesEmail_whenExists() {
                when(emailRepository.existsById(1L)).thenReturn(true);

                emailService.deleteById(1L);

                verify(emailRepository).deleteById(1L);
            }

            @Test
            @DisplayName("Should throw EmailNotFoundException when email does not exist")
            void deleteById_throwsEmailNotFoundException_whenNotFound() {
                when(emailRepository.existsById(99L)).thenReturn(false);

                assertThatThrownBy(() -> emailService.deleteById(99L))
                        .isInstanceOf(EmailNotFoundException.class)
                        .hasMessageContaining("99");

                verify(emailRepository, never()).deleteById(anyLong());
            }
        }

        // ─── findByStatus ────────────────────────────────────────────────────────

        @Nested
        @DisplayName("findByStatus()")
        class FindByStatus {

            @Test
            @DisplayName("Should return emails filtered by status")
            void findByStatus_returnsMatchingEmails() {
                List<Email> emails = List.of(email);
                List<EmailResponseDto> expected = List.of(responseDto);

                when(emailRepository.findByStatusOrderByCreatedAtDesc(EmailStatus.DRAFT)).thenReturn(emails);
                when(emailMapper.toResponseList(emails)).thenReturn(expected);

                List<EmailResponseDto> result = emailService.findByStatus(EmailStatus.DRAFT);

                assertThat(result).containsExactly(responseDto);
            }

            @Test
            @DisplayName("Should return empty list when no emails match status")
            void findByStatus_returnsEmptyList_whenNoMatch() {
                when(emailRepository.findByStatusOrderByCreatedAtDesc(EmailStatus.SENT))
                        .thenReturn(Collections.emptyList());
                when(emailMapper.toResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

                assertThat(emailService.findByStatus(EmailStatus.SENT)).isEmpty();
            }
        }

        // ─── findBySender_Id ─────────────────────────────────────────────────────

        @Nested
        @DisplayName("findBySender_Id()")
        class FindBySenderId {

            @Test
            @DisplayName("Should return emails by sender id")
            void findBySenderId_returnsEmails() {
                List<Email> emails = List.of(email);
                List<EmailResponseDto> expected = List.of(responseDto);

                when(emailRepository.findBySender_Id(1L)).thenReturn(emails);
                when(emailMapper.toResponseList(emails)).thenReturn(expected);

                List<EmailResponseDto> result = emailService.findBySender_Id(1L);

                assertThat(result).containsExactly(responseDto);
            }

            @Test
            @DisplayName("Should return empty list when sender has no emails")
            void findBySenderId_returnsEmptyList_whenNoEmails() {
                when(emailRepository.findBySender_Id(99L)).thenReturn(Collections.emptyList());
                when(emailMapper.toResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

                assertThat(emailService.findBySender_Id(99L)).isEmpty();
            }
        }

        // ─── findBySubjectContainingIgnoreCase ───────────────────────────────────

        @Nested
        @DisplayName("findBySubjectContainingIgnoreCase()")
        class FindBySubject {

            @Test
            @DisplayName("Should return emails matching keyword")
            void findBySubject_returnsMatchingEmails() {
                List<Email> emails = List.of(email);
                List<EmailResponseDto> expected = List.of(responseDto);

                when(emailRepository.findBySubjectContainingIgnoreCase("test")).thenReturn(emails);
                when(emailMapper.toResponseList(emails)).thenReturn(expected);

                List<EmailResponseDto> result = emailService.findBySubjectContainingIgnoreCase("test");

                assertThat(result).containsExactly(responseDto);
            }

            @Test
            @DisplayName("Should return empty list when no subject matches keyword")
            void findBySubject_returnsEmptyList_whenNoMatch() {
                when(emailRepository.findBySubjectContainingIgnoreCase("xyz"))
                        .thenReturn(Collections.emptyList());
                when(emailMapper.toResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

                assertThat(emailService.findBySubjectContainingIgnoreCase("xyz")).isEmpty();
            }
        }

        // ─── findByReadFalse ─────────────────────────────────────────────────────

        @Nested
        @DisplayName("findByReadFalse()")
        class FindByReadFalse {

            @Test
            @DisplayName("Should return only unread emails")
            void findByReadFalse_returnsUnreadEmails() {
                List<Email> emails = List.of(email);
                List<EmailResponseDto> expected = List.of(responseDto);

                when(emailRepository.findByReadFalseOrderByCreatedAtDesc()).thenReturn(emails);
                when(emailMapper.toResponseList(emails)).thenReturn(expected);

                List<EmailResponseDto> result = emailService.findByReadFalse();

                assertThat(result).containsExactly(responseDto);
            }

            @Test
            @DisplayName("Should return empty list when all emails are read")
            void findByReadFalse_returnsEmptyList_whenAllRead() {
                when(emailRepository.findByReadFalseOrderByCreatedAtDesc()).thenReturn(Collections.emptyList());
                when(emailMapper.toResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

                assertThat(emailService.findByReadFalse()).isEmpty();
            }
        }

        // ─── findByCampaign_Id ───────────────────────────────────────────────────

        @Nested
        @DisplayName("findByCampaign_Id()")
        class FindByCampaignId {

            @Test
            @DisplayName("Should return emails belonging to campaign")
            void findByCampaignId_returnsEmails() {
                List<Email> emails = List.of(email);
                List<EmailResponseDto> expected = List.of(responseDto);

                when(emailRepository.findByCampaign_Id(1L)).thenReturn(emails);
                when(emailMapper.toResponseList(emails)).thenReturn(expected);

                List<EmailResponseDto> result = emailService.findByCampaign_Id(1L);

                assertThat(result).containsExactly(responseDto);
            }

            @Test
            @DisplayName("Should return empty list when campaign has no emails")
            void findByCampaignId_returnsEmptyList_whenNone() {
                when(emailRepository.findByCampaign_Id(99L)).thenReturn(Collections.emptyList());
                when(emailMapper.toResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

                assertThat(emailService.findByCampaign_Id(99L)).isEmpty();
            }
        }

        // ─── markAsRead ──────────────────────────────────────────────────────────

        @Nested
        @DisplayName("markAsRead()")
        class MarkAsRead {

            @Test
            @DisplayName("Should set read=true and return updated email")
            void markAsRead_setsReadTrue_andReturnsDto() {
                when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
                when(emailRepository.save(email)).thenReturn(email);
                when(emailMapper.toResponse(email)).thenReturn(responseDto);

                EmailResponseDto result = emailService.markAsRead(1L);

                assertThat(email.isRead()).isTrue();
                assertThat(result).isEqualTo(responseDto);
                verify(emailRepository).save(email);
            }

            @Test
            @DisplayName("Should throw EmailNotFoundException when email not found")
            void markAsRead_throwsEmailNotFoundException_whenNotFound() {
                when(emailRepository.findById(99L)).thenReturn(Optional.empty());

                assertThatThrownBy(() -> emailService.markAsRead(99L))
                        .isInstanceOf(EmailNotFoundException.class)
                        .hasMessageContaining("99");

                verify(emailRepository, never()).save(any());
            }
        }
    }
}
