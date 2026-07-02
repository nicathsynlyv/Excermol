package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.FormResponseServiceImpl;
import com.example.Excermol.entity.*;
import com.example.Excermol.entity.dtos.FormAnswerRequestDTO;
import com.example.Excermol.entity.dtos.FormSubmitRequestDTO;
import com.example.Excermol.entity.dtos.FormSubmitResponseDTO;
import com.example.Excermol.exception.FormFieldNotFoundException;
import com.example.Excermol.exception.FormNotFoundException;
import com.example.Excermol.exception.FormResponseNotFoundException;
import com.example.Excermol.exception.PersonNotFoundException;
import com.example.Excermol.mapper.FormAnswerMapper;
import com.example.Excermol.mapper.FormResponseMapper;
import com.example.Excermol.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@DisplayName("FormResponseServiceImplTest Unit Tests")
public class FormResponseServiceImplTest {
    @Mock
    private FormResponseRepository formResponseRepository;

    @Mock
    private FormResponseAnswerRepository formResponseAnswerRepository;

    @Mock
    private FormRepository formRepository;

    @Mock
    private FormFieldRepository formFieldRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FormResponseMapper formResponseMapper;

    @Mock
    private FormAnswerMapper formAnswerMapper;

    @InjectMocks
    private FormResponseServiceImpl formResponseService;

    private Form form;
    private Person contact;
    private FormField formField;
    private FormResponse formResponse;
    private FormResponseAnswer answer;
    private FormSubmitRequestDTO submitDTO;
    private FormAnswerRequestDTO answerRequestDTO;
    private FormSubmitResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        form = new Form();
        form.setId(1L);
        form.setResponsesCount(5);

        contact = new Person();
        contact.setId(1L);
        contact.setFullName("Nicat");
        contact.setLastName("Aliyev");
        contact.setEmail("nicat@example.com");

        formField = new FormField();
        formField.setId(1L);
        formField.setLabel("Work Email Address");

        formResponse = new FormResponse();
        formResponse.setId(1L);
        formResponse.setForm(form);
        formResponse.setContact(contact);

        answer = new FormResponseAnswer();
        answer.setId(1L);
        answer.setValue("test@example.com");

        answerRequestDTO = new FormAnswerRequestDTO();
        answerRequestDTO.setFormFieldId(1L);
        answerRequestDTO.setValue("test@example.com");

        submitDTO = new FormSubmitRequestDTO();
        submitDTO.setFormId(1L);
        submitDTO.setContactId(1L);
        submitDTO.setAnswers(new ArrayList<>(List.of(answerRequestDTO)));

        responseDTO = new FormSubmitResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setFormId(1L);
        responseDTO.setContactId(1L);
    }

    // ---------- submitForm ----------

    @Test
    void submitForm_success_withContactAndAnswers() {
        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formResponseMapper.toEntity(submitDTO)).thenReturn(formResponse);
        when(personRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(formResponseRepository.save(formResponse)).thenReturn(formResponse);
        when(formFieldRepository.findById(1L)).thenReturn(Optional.of(formField));
        when(formAnswerMapper.toEntity(answerRequestDTO)).thenReturn(answer);
        when(formRepository.save(form)).thenReturn(form);
        when(formResponseRepository.findById(1L)).thenReturn(Optional.of(formResponse));
        when(formResponseMapper.toResponseDTO(formResponse)).thenReturn(responseDTO);

        FormSubmitResponseDTO result = formResponseService.submitForm(submitDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(personRepository, times(1)).findById(1L);
        verify(formResponseAnswerRepository, times(1)).save(answer);
        verify(formRepository, times(1)).save(form);
        assertEquals(6, form.getResponsesCount());
    }

    @Test
    void submitForm_success_withoutContact() {
        submitDTO.setContactId(null);

        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formResponseMapper.toEntity(submitDTO)).thenReturn(formResponse);
        when(formResponseRepository.save(formResponse)).thenReturn(formResponse);
        when(formFieldRepository.findById(1L)).thenReturn(Optional.of(formField));
        when(formAnswerMapper.toEntity(answerRequestDTO)).thenReturn(answer);
        when(formRepository.save(form)).thenReturn(form);
        when(formResponseRepository.findById(1L)).thenReturn(Optional.of(formResponse));
        when(formResponseMapper.toResponseDTO(formResponse)).thenReturn(responseDTO);

        FormSubmitResponseDTO result = formResponseService.submitForm(submitDTO);

        assertNotNull(result);
        verify(personRepository, never()).findById(any());
    }

    @Test
    void submitForm_success_withoutAnswers() {
        submitDTO.setAnswers(null);

        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formResponseMapper.toEntity(submitDTO)).thenReturn(formResponse);
        when(personRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(formResponseRepository.save(formResponse)).thenReturn(formResponse);
        when(formRepository.save(form)).thenReturn(form);
        when(formResponseRepository.findById(1L)).thenReturn(Optional.of(formResponse));
        when(formResponseMapper.toResponseDTO(formResponse)).thenReturn(responseDTO);

        FormSubmitResponseDTO result = formResponseService.submitForm(submitDTO);

        assertNotNull(result);
        verify(formFieldRepository, never()).findById(any());
        verify(formResponseAnswerRepository, never()).save(any());
    }

    @Test
    void submitForm_formNotFound_throwsException() {
        when(formRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FormNotFoundException.class, () -> formResponseService.submitForm(submitDTO));

        verify(formResponseRepository, never()).save(any());
    }

    @Test
    void submitForm_contactNotFound_throwsException() {
        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formResponseMapper.toEntity(submitDTO)).thenReturn(formResponse);
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> formResponseService.submitForm(submitDTO));

        verify(formResponseRepository, never()).save(any());
    }

    @Test
    void submitForm_formFieldNotFound_throwsException() {
        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formResponseMapper.toEntity(submitDTO)).thenReturn(formResponse);
        when(personRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(formResponseRepository.save(formResponse)).thenReturn(formResponse);
        when(formFieldRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FormFieldNotFoundException.class, () -> formResponseService.submitForm(submitDTO));

        verify(formResponseAnswerRepository, never()).save(any());
        verify(formRepository, never()).save(form);
    }

    @Test
    void submitForm_multipleAnswers_allSaved() {
        FormAnswerRequestDTO answer2Dto = new FormAnswerRequestDTO();
        answer2Dto.setFormFieldId(2L);
        answer2Dto.setValue("Nicat");

        FormField formField2 = new FormField();
        formField2.setId(2L);

        FormResponseAnswer answer2 = new FormResponseAnswer();
        answer2.setId(2L);
        answer2.setValue("Nicat");

        submitDTO.setAnswers(new ArrayList<>(Arrays.asList(answerRequestDTO, answer2Dto)));

        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formResponseMapper.toEntity(submitDTO)).thenReturn(formResponse);
        when(personRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(formResponseRepository.save(formResponse)).thenReturn(formResponse);
        when(formFieldRepository.findById(1L)).thenReturn(Optional.of(formField));
        when(formFieldRepository.findById(2L)).thenReturn(Optional.of(formField2));
        when(formAnswerMapper.toEntity(answerRequestDTO)).thenReturn(answer);
        when(formAnswerMapper.toEntity(answer2Dto)).thenReturn(answer2);
        when(formRepository.save(form)).thenReturn(form);
        when(formResponseRepository.findById(1L)).thenReturn(Optional.of(formResponse));
        when(formResponseMapper.toResponseDTO(formResponse)).thenReturn(responseDTO);

        formResponseService.submitForm(submitDTO);

        verify(formResponseAnswerRepository, times(1)).save(answer);
        verify(formResponseAnswerRepository, times(1)).save(answer2);
    }

    // ---------- getResponsesByFormId ----------

    @Test
    void getResponsesByFormId_success() {
        when(formResponseRepository.findAllByFormId(1L)).thenReturn(List.of(formResponse));
        when(formResponseMapper.toResponseDTO(formResponse)).thenReturn(responseDTO);

        List<FormSubmitResponseDTO> result = formResponseService.getResponsesByFormId(1L);

        assertEquals(1, result.size());
        verify(formResponseRepository, times(1)).findAllByFormId(1L);
    }

    @Test
    void getResponsesByFormId_emptyList() {
        when(formResponseRepository.findAllByFormId(1L)).thenReturn(List.of());

        List<FormSubmitResponseDTO> result = formResponseService.getResponsesByFormId(1L);

        assertTrue(result.isEmpty());
        verify(formResponseMapper, never()).toResponseDTO(any());
    }

    // ---------- getResponseById ----------

    @Test
    void getResponseById_success() {
        when(formResponseRepository.findById(1L)).thenReturn(Optional.of(formResponse));
        when(formResponseMapper.toResponseDTO(formResponse)).thenReturn(responseDTO);

        FormSubmitResponseDTO result = formResponseService.getResponseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getResponseById_notFound_throwsException() {
        when(formResponseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FormResponseNotFoundException.class, () -> formResponseService.getResponseById(1L));
    }

    // ---------- deleteResponse ----------

    @Test
    void deleteResponse_success_decrementsCount() {
        when(formResponseRepository.findById(1L)).thenReturn(Optional.of(formResponse));
        when(formRepository.save(form)).thenReturn(form);
        doNothing().when(formResponseRepository).delete(formResponse);

        formResponseService.deleteResponse(1L);

        assertEquals(4, form.getResponsesCount());
        verify(formRepository, times(1)).save(form);
        verify(formResponseRepository, times(1)).delete(formResponse);
    }

    @Test
    void deleteResponse_countAlreadyZero_doesNotGoNegative() {
        form.setResponsesCount(0);
        when(formResponseRepository.findById(1L)).thenReturn(Optional.of(formResponse));
        doNothing().when(formResponseRepository).delete(formResponse);

        formResponseService.deleteResponse(1L);

        assertEquals(0, form.getResponsesCount());
        verify(formRepository, never()).save(form);
        verify(formResponseRepository, times(1)).delete(formResponse);
    }

    @Test
    void deleteResponse_notFound_throwsException() {
        when(formResponseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FormResponseNotFoundException.class, () -> formResponseService.deleteResponse(1L));

        verify(formResponseRepository, never()).delete(any());
    }
}
