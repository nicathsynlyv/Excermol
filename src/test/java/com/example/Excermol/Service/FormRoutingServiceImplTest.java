package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.FormRoutingServiceImpl;
import com.example.Excermol.entity.Email;
import com.example.Excermol.entity.Form;
import com.example.Excermol.entity.FormField;
import com.example.Excermol.entity.FormRouting;
import com.example.Excermol.entity.dtos.FormRoutingCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormRoutingResponseDTO;
import com.example.Excermol.entity.dtos.FormRoutingUpdateRequestDTO;
import com.example.Excermol.enums.RoutingCondition;
import com.example.Excermol.exception.EmailNotFoundException;
import com.example.Excermol.exception.FormFieldNotFoundException;
import com.example.Excermol.exception.FormNotFoundException;
import com.example.Excermol.exception.FormRoutingNotFoundException;
import com.example.Excermol.mapper.FormRoutingMapper;
import com.example.Excermol.repository.EmailRepository;
import com.example.Excermol.repository.FormFieldRepository;
import com.example.Excermol.repository.FormRepository;
import com.example.Excermol.repository.FormRoutingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FormRoutingServiceImplTest Unit Tests")
public class FormRoutingServiceImplTest {
    @Mock
    private FormRoutingRepository formRoutingRepository;

    @Mock
    private FormRepository formRepository;

    @Mock
    private FormFieldRepository formFieldRepository;

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private FormRoutingMapper formRoutingMapper;

    @InjectMocks
    private FormRoutingServiceImpl formRoutingService;

    private Form form;
    private FormField formField;
    private FormField formField2;
    private Email email;
    private FormRouting formRouting;
    private FormRoutingCreateRequestDTO createDTO;
    private FormRoutingUpdateRequestDTO updateDTO;
    private FormRoutingResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        form = new Form();
        form.setId(1L);

        formField = new FormField();
        formField.setId(1L);
        formField.setLabel("Work Email Address");

        formField2 = new FormField();
        formField2.setId(2L);
        formField2.setLabel("Personal Email");

        email = new Email();
        email.setId(1L);
        email.setSubject("Welcome");

        formRouting = new FormRouting();
        formRouting.setId(1L);
        formRouting.setConditionType(RoutingCondition.CONTAINS);
        formRouting.setConditionValue("@gmail.com");
        formRouting.setRedirectTo("Thank you page");
        formRouting.setRoutingOrder(1);
        formRouting.setForm(form);
        formRouting.setFormField(formField);

        createDTO = new FormRoutingCreateRequestDTO();
        createDTO.setConditionType(RoutingCondition.CONTAINS);
        createDTO.setConditionValue("@gmail.com");
        createDTO.setRedirectTo("Thank you page");
        createDTO.setRoutingOrder(1);
        createDTO.setFormId(1L);
        createDTO.setFormFieldId(1L);
        createDTO.setEmailId(1L);

        updateDTO = new FormRoutingUpdateRequestDTO();
        updateDTO.setConditionValue("@company.com");
        updateDTO.setRedirectTo("New page");

        responseDTO = new FormRoutingResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setConditionType(RoutingCondition.CONTAINS);
        responseDTO.setConditionValue("@gmail.com");
        responseDTO.setFormId(1L);
        responseDTO.setFormFieldId(1L);
    }

    // ---------- createRouting ----------

    @Test
    void createRouting_success_withEmail() {
        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formFieldRepository.findById(1L)).thenReturn(Optional.of(formField));
        when(formRoutingMapper.toEntity(createDTO)).thenReturn(formRouting);
        when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
        when(formRoutingRepository.save(formRouting)).thenReturn(formRouting);
        when(formRoutingMapper.toResponseDTO(formRouting)).thenReturn(responseDTO);

        FormRoutingResponseDTO result = formRoutingService.createRouting(createDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(emailRepository, times(1)).findById(1L);
        verify(formRoutingRepository, times(1)).save(formRouting);
    }

    @Test
    void createRouting_success_withoutEmail() {
        createDTO.setEmailId(null);

        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formFieldRepository.findById(1L)).thenReturn(Optional.of(formField));
        when(formRoutingMapper.toEntity(createDTO)).thenReturn(formRouting);
        when(formRoutingRepository.save(formRouting)).thenReturn(formRouting);
        when(formRoutingMapper.toResponseDTO(formRouting)).thenReturn(responseDTO);

        FormRoutingResponseDTO result = formRoutingService.createRouting(createDTO);

        assertNotNull(result);
        verify(emailRepository, never()).findById(any());
    }

    @Test
    void createRouting_formNotFound_throwsException() {
        when(formRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FormNotFoundException.class, () -> formRoutingService.createRouting(createDTO));

        verify(formFieldRepository, never()).findById(any());
        verify(formRoutingRepository, never()).save(any());
    }

    @Test
    void createRouting_formFieldNotFound_throwsException() {
        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formFieldRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FormFieldNotFoundException.class, () -> formRoutingService.createRouting(createDTO));

        verify(formRoutingRepository, never()).save(any());
    }

    @Test
    void createRouting_emailNotFound_throwsException() {
        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formFieldRepository.findById(1L)).thenReturn(Optional.of(formField));
        when(formRoutingMapper.toEntity(createDTO)).thenReturn(formRouting);
        when(emailRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> formRoutingService.createRouting(createDTO));

        verify(formRoutingRepository, never()).save(any());
    }

    // ---------- getRoutingsByFormId ----------

    @Test
    void getRoutingsByFormId_success() {
        when(formRoutingRepository.findAllByFormIdOrderByRoutingOrderAsc(1L))
                .thenReturn(List.of(formRouting));
        when(formRoutingMapper.toResponseDTO(formRouting)).thenReturn(responseDTO);

        List<FormRoutingResponseDTO> result = formRoutingService.getRoutingsByFormId(1L);

        assertEquals(1, result.size());
        verify(formRoutingRepository, times(1)).findAllByFormIdOrderByRoutingOrderAsc(1L);
    }

    @Test
    void getRoutingsByFormId_emptyList() {
        when(formRoutingRepository.findAllByFormIdOrderByRoutingOrderAsc(1L)).thenReturn(List.of());

        List<FormRoutingResponseDTO> result = formRoutingService.getRoutingsByFormId(1L);

        assertTrue(result.isEmpty());
        verify(formRoutingMapper, never()).toResponseDTO(any());
    }

    // ---------- getRoutingById ----------

    @Test
    void getRoutingById_success() {
        when(formRoutingRepository.findById(1L)).thenReturn(Optional.of(formRouting));
        when(formRoutingMapper.toResponseDTO(formRouting)).thenReturn(responseDTO);

        FormRoutingResponseDTO result = formRoutingService.getRoutingById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getRoutingById_notFound_throwsException() {
        when(formRoutingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FormRoutingNotFoundException.class, () -> formRoutingService.getRoutingById(1L));
    }

    // ---------- updateRouting ----------

    @Test
    void updateRouting_success_noOptionalFields() {
        when(formRoutingRepository.findById(1L)).thenReturn(Optional.of(formRouting));
        when(formRoutingRepository.save(formRouting)).thenReturn(formRouting);
        when(formRoutingMapper.toResponseDTO(formRouting)).thenReturn(responseDTO);

        FormRoutingResponseDTO result = formRoutingService.updateRouting(1L, updateDTO);

        assertNotNull(result);
        verify(formRoutingMapper, times(1)).updateEntity(formRouting, updateDTO);
        verify(formFieldRepository, never()).findById(any());
        verify(emailRepository, never()).findById(any());
        verify(formRoutingRepository, times(1)).save(formRouting);
    }

    @Test
    void updateRouting_success_withFormFieldUpdate() {
        updateDTO.setFormFieldId(2L);

        when(formRoutingRepository.findById(1L)).thenReturn(Optional.of(formRouting));
        when(formFieldRepository.findById(2L)).thenReturn(Optional.of(formField2));
        when(formRoutingRepository.save(formRouting)).thenReturn(formRouting);
        when(formRoutingMapper.toResponseDTO(formRouting)).thenReturn(responseDTO);

        formRoutingService.updateRouting(1L, updateDTO);

        assertEquals(formField2, formRouting.getFormField());
        verify(formFieldRepository, times(1)).findById(2L);
    }

    @Test
    void updateRouting_success_withEmailUpdate() {
        updateDTO.setEmailId(1L);

        when(formRoutingRepository.findById(1L)).thenReturn(Optional.of(formRouting));
        when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
        when(formRoutingRepository.save(formRouting)).thenReturn(formRouting);
        when(formRoutingMapper.toResponseDTO(formRouting)).thenReturn(responseDTO);

        formRoutingService.updateRouting(1L, updateDTO);

        assertEquals(email, formRouting.getEmail());
        verify(emailRepository, times(1)).findById(1L);
    }

    @Test
    void updateRouting_notFound_throwsException() {
        when(formRoutingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FormRoutingNotFoundException.class, () -> formRoutingService.updateRouting(1L, updateDTO));

        verify(formRoutingMapper, never()).updateEntity(any(), any());
        verify(formRoutingRepository, never()).save(any());
    }

    @Test
    void updateRouting_formFieldNotFound_throwsException() {
        updateDTO.setFormFieldId(99L);

        when(formRoutingRepository.findById(1L)).thenReturn(Optional.of(formRouting));
        when(formFieldRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(FormFieldNotFoundException.class, () -> formRoutingService.updateRouting(1L, updateDTO));

        verify(formRoutingRepository, never()).save(any());
    }

    @Test
    void updateRouting_emailNotFound_throwsException() {
        updateDTO.setEmailId(99L);

        when(formRoutingRepository.findById(1L)).thenReturn(Optional.of(formRouting));
        when(emailRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> formRoutingService.updateRouting(1L, updateDTO));

        verify(formRoutingRepository, never()).save(any());
    }

    // ---------- deleteRouting ----------

    @Test
    void deleteRouting_success() {
        when(formRoutingRepository.findById(1L)).thenReturn(Optional.of(formRouting));
        doNothing().when(formRoutingRepository).delete(formRouting);

        formRoutingService.deleteRouting(1L);

        verify(formRoutingRepository, times(1)).delete(formRouting);
    }

    @Test
    void deleteRouting_notFound_throwsException() {
        when(formRoutingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FormRoutingNotFoundException.class, () -> formRoutingService.deleteRouting(1L));

        verify(formRoutingRepository, never()).delete(any());
    }
}
