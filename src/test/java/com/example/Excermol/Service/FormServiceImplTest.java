package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.FormServiceImpl;
import com.example.Excermol.entity.Form;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.FormCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormResponseDTO;
import com.example.Excermol.entity.dtos.FormUpdateRequestDTO;
import com.example.Excermol.enums.FormStatus;
import com.example.Excermol.exception.FormNotFoundException;
import com.example.Excermol.exception.UserNotFoundException;
import com.example.Excermol.mapper.FormMapper;
import com.example.Excermol.repository.FormRepository;
import com.example.Excermol.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FormServiceImplTest Unit Tests")
public class FormServiceImplTest {
    @Mock
    private FormRepository formRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FormMapper formMapper;

    @InjectMocks
    private FormServiceImpl formService;

    private Form form;
    private FormCreateRequestDTO createDTO;
    private FormUpdateRequestDTO updateDTO;
    private FormResponseDTO responseDTO;
    private User owner;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(5L);

        form = new Form();
        form.setId(1L);
        form.setFormsName("Test Form");
        form.setStatus(FormStatus.DRAFT);

        createDTO = new FormCreateRequestDTO();
        createDTO.setFormsName("Test Form");

        updateDTO = new FormUpdateRequestDTO();
        updateDTO.setFormsName("Updated Form");

        responseDTO = new FormResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setFormsName("Test Form");
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void createForm_shouldSaveAndReturnForm() {
        when(userRepository.findById(5L)).thenReturn(Optional.of(owner));
        when(formMapper.toEntity(createDTO)).thenReturn(form);
        when(formRepository.save(form)).thenReturn(form);
        when(formMapper.toResponseDTO(form)).thenReturn(responseDTO);

        FormResponseDTO result = formService.createForm(createDTO, 5L);

        assertThat(result).isNotNull();
        assertThat(result.getFormsName()).isEqualTo("Test Form");
        assertThat(form.getOwner()).isEqualTo(owner);
        verify(formRepository).save(form);
    }

    @Test
    void createForm_shouldThrowException_whenOwnerNotFound() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> formService.createForm(createDTO, 5L));
        verify(formRepository, never()).save(any());
    }

    // =========================
    // GET ALL
    // =========================
    @Test
    void getAllForms_shouldReturnAllForms() {
        when(formRepository.findAll()).thenReturn(List.of(form));
        when(formMapper.toResponseDTO(form)).thenReturn(responseDTO);

        List<FormResponseDTO> result = formService.getAllForms();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFormsName()).isEqualTo("Test Form");
        verify(formRepository).findAll();
    }

    @Test
    void getAllForms_shouldReturnEmptyList_whenNoForms() {
        when(formRepository.findAll()).thenReturn(List.of());

        List<FormResponseDTO> result = formService.getAllForms();

        assertThat(result).isEmpty();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getFormById_shouldReturnForm_whenExists() {
        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formMapper.toResponseDTO(form)).thenReturn(responseDTO);

        FormResponseDTO result = formService.getFormById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(formRepository).findById(1L);
    }

    @Test
    void getFormById_shouldThrowException_whenNotFound() {
        when(formRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(FormNotFoundException.class, () -> formService.getFormById(99L));
    }

    // =========================
    // GET BY OWNER
    // =========================
    @Test
    void getFormsByOwner_shouldReturnFilteredForms() {
        when(formRepository.findAllByOwnerId(5L)).thenReturn(List.of(form));
        when(formMapper.toResponseDTO(form)).thenReturn(responseDTO);

        List<FormResponseDTO> result = formService.getFormsByOwner(5L);

        assertThat(result).hasSize(1);
        verify(formRepository).findAllByOwnerId(5L);
    }

    @Test
    void getFormsByOwner_shouldReturnEmptyList_whenNoForms() {
        when(formRepository.findAllByOwnerId(5L)).thenReturn(List.of());

        List<FormResponseDTO> result = formService.getFormsByOwner(5L);

        assertThat(result).isEmpty();
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void updateForm_shouldUpdateAndReturnForm() {
        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formRepository.save(form)).thenReturn(form);
        when(formMapper.toResponseDTO(form)).thenReturn(responseDTO);

        FormResponseDTO result = formService.updateForm(1L, updateDTO);

        assertThat(result).isNotNull();
        verify(formMapper).updateEntity(form, updateDTO);
        verify(formRepository).save(form);
    }

    @Test
    void updateForm_shouldThrowException_whenNotFound() {
        when(formRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(FormNotFoundException.class, () -> formService.updateForm(99L, updateDTO));
        verify(formRepository, never()).save(any());
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void deleteForm_shouldDeleteForm_whenExists() {
        when(formRepository.findById(1L)).thenReturn(Optional.of(form));

        formService.deleteForm(1L);

        verify(formRepository).delete(form);
    }

    @Test
    void deleteForm_shouldThrowException_whenNotFound() {
        when(formRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(FormNotFoundException.class, () -> formService.deleteForm(99L));
        verify(formRepository, never()).delete(any());
    }

    // =========================
    // TOGGLE STATUS
    // =========================
    @Test
    void toggleFormStatus_shouldSetToDraft_whenCurrentlyPublished() {
        form.setStatus(FormStatus.PUBLISHED);

        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formRepository.save(form)).thenReturn(form);
        when(formMapper.toResponseDTO(form)).thenReturn(responseDTO);

        formService.toggleFormStatus(1L);

        assertThat(form.getStatus()).isEqualTo(FormStatus.DRAFT);
        verify(formRepository).save(form);
    }

    @Test
    void toggleFormStatus_shouldSetToPublished_whenCurrentlyDraft() {
        form.setStatus(FormStatus.DRAFT);

        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(formRepository.save(form)).thenReturn(form);
        when(formMapper.toResponseDTO(form)).thenReturn(responseDTO);

        formService.toggleFormStatus(1L);

        assertThat(form.getStatus()).isEqualTo(FormStatus.PUBLISHED);
        verify(formRepository).save(form);
    }

    @Test
    void toggleFormStatus_shouldThrowException_whenNotFound() {
        when(formRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(FormNotFoundException.class, () -> formService.toggleFormStatus(99L));
        verify(formRepository, never()).save(any());
    }
}
