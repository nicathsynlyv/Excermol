package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.FormFieldServiceImpl;
import com.example.Excermol.entity.Form;
import com.example.Excermol.entity.FormField;
import com.example.Excermol.entity.dtos.FormFieldCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormFieldResponseDTO;
import com.example.Excermol.entity.dtos.FormFieldUpdateRequestDTO;
import com.example.Excermol.enums.FieldType;
import com.example.Excermol.exception.FormFieldNotFoundException;
import com.example.Excermol.exception.FormNotFoundException;
import com.example.Excermol.mapper.FormFieldMapper;
import com.example.Excermol.repository.FormFieldRepository;
import com.example.Excermol.repository.FormRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FormFieldServiceImplTest Unit Tests")
public class FormFieldServiceImplTest {
    @Mock
    private FormFieldRepository formFieldRepository;

    @Mock
    private FormRepository formRepository;

    @Mock
    private FormFieldMapper formFieldMapper;

    @InjectMocks
    private FormFieldServiceImpl formFieldService;

    private Form form;
    private FormField formField;
    private FormFieldCreateRequestDTO createDTO;
    private FormFieldUpdateRequestDTO updateDTO;
    private FormFieldResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        form = new Form();
        form.setId(1L);

        formField = new FormField();
        formField.setId(1L);
        formField.setLabel("Work Email Address");
        formField.setPlaceholder("Enter email address");
        formField.setFieldType(FieldType.EMAIL);
        formField.setIsRequired(true);
        formField.setFieldOrder(1);
        formField.setForm(form);

        createDTO = new FormFieldCreateRequestDTO();
        createDTO.setLabel("Work Email Address");
        createDTO.setPlaceholder("Enter email address");
        createDTO.setFieldType(FieldType.EMAIL);
        createDTO.setIsRequired(true);
        createDTO.setFieldOrder(1);
        createDTO.setFormId(1L);

        updateDTO = new FormFieldUpdateRequestDTO();
        updateDTO.setLabel("Updated Label");
        updateDTO.setIsRequired(false);

        responseDTO = new FormFieldResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setLabel("Work Email Address");
        responseDTO.setPlaceholder("Enter email address");
        responseDTO.setFieldType(FieldType.EMAIL);
        responseDTO.setIsRequired(true);
        responseDTO.setFieldOrder(1);
        responseDTO.setFormId(1L);
    }

    // ---------- createField ----------

    @Test
    void createField_success() {
        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formFieldMapper.toEntity(createDTO)).thenReturn(formField);
        when(formFieldRepository.save(formField)).thenReturn(formField);
        when(formFieldMapper.toResponseDTO(formField)).thenReturn(responseDTO);

        FormFieldResponseDTO result = formFieldService.createField(createDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getLabel(), result.getLabel());
        verify(formFieldRepository, times(1)).save(formField);
        verify(formRepository, times(1)).findById(1L);
    }

    @Test
    void createField_formNotFound_throwsException() {
        when(formRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FormNotFoundException.class, () -> formFieldService.createField(createDTO));

        verify(formFieldRepository, never()).save(any());
    }

    // ---------- getFieldsByFormId ----------

    @Test
    void getFieldsByFormId_success() {
        FormField field2 = new FormField();
        field2.setId(2L);
        field2.setLabel("First Name");
        field2.setFieldOrder(2);

        FormFieldResponseDTO responseDTO2 = new FormFieldResponseDTO();
        responseDTO2.setId(2L);
        responseDTO2.setLabel("First Name");
        responseDTO2.setFieldOrder(2);

        when(formFieldRepository.findAllByFormIdOrderByFieldOrderAsc(1L))
                .thenReturn(Arrays.asList(formField, field2));
        when(formFieldMapper.toResponseDTO(formField)).thenReturn(responseDTO);
        when(formFieldMapper.toResponseDTO(field2)).thenReturn(responseDTO2);

        List<FormFieldResponseDTO> result = formFieldService.getFieldsByFormId(1L);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getFieldOrder());
        assertEquals(2, result.get(1).getFieldOrder());
        verify(formFieldRepository, times(1)).findAllByFormIdOrderByFieldOrderAsc(1L);
    }

    @Test
    void getFieldsByFormId_emptyList() {
        when(formFieldRepository.findAllByFormIdOrderByFieldOrderAsc(1L)).thenReturn(List.of());

        List<FormFieldResponseDTO> result = formFieldService.getFieldsByFormId(1L);

        assertTrue(result.isEmpty());
        verify(formFieldMapper, never()).toResponseDTO(any());
    }

    // ---------- getFieldById ----------

    @Test
    void getFieldById_success() {
        when(formFieldRepository.findById(1L)).thenReturn(Optional.of(formField));
        when(formFieldMapper.toResponseDTO(formField)).thenReturn(responseDTO);

        FormFieldResponseDTO result = formFieldService.getFieldById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getFieldById_notFound_throwsException() {
        when(formFieldRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FormFieldNotFoundException.class, () -> formFieldService.getFieldById(1L));
    }

    // ---------- updateField ----------

    @Test
    void updateField_success() {
        when(formFieldRepository.findById(1L)).thenReturn(Optional.of(formField));
        when(formFieldRepository.save(formField)).thenReturn(formField);
        when(formFieldMapper.toResponseDTO(formField)).thenReturn(responseDTO);

        FormFieldResponseDTO result = formFieldService.updateField(1L, updateDTO);

        assertNotNull(result);
        verify(formFieldMapper, times(1)).updateEntity(formField, updateDTO);
        verify(formFieldRepository, times(1)).save(formField);
    }

    @Test
    void updateField_notFound_throwsException() {
        when(formFieldRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FormFieldNotFoundException.class, () -> formFieldService.updateField(1L, updateDTO));

        verify(formFieldMapper, never()).updateEntity(any(), any());
        verify(formFieldRepository, never()).save(any());
    }

    // ---------- deleteField ----------

    @Test
    void deleteField_success() {
        when(formFieldRepository.findById(1L)).thenReturn(Optional.of(formField));
        doNothing().when(formFieldRepository).delete(formField);

        formFieldService.deleteField(1L);

        verify(formFieldRepository, times(1)).delete(formField);
    }

    @Test
    void deleteField_notFound_throwsException() {
        when(formFieldRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FormFieldNotFoundException.class, () -> formFieldService.deleteField(1L));

        verify(formFieldRepository, never()).delete(any());
    }
}
