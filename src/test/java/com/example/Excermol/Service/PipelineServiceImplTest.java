package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.PipelineServiceImpl;
import com.example.Excermol.entity.Company;
import com.example.Excermol.entity.Pipeline;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.PipelineRequestDTO;
import com.example.Excermol.entity.dtos.PipelineResponseDTO;
import com.example.Excermol.enums.PipelineStage;
import com.example.Excermol.enums.PipelineStatus;
import com.example.Excermol.exception.CompanyNotFoundException;
import com.example.Excermol.exception.PipelineNotFoundException;
import com.example.Excermol.mapper.PipelineMapper;
import com.example.Excermol.repository.CompanyRepository;
import com.example.Excermol.repository.PipelineRepository;
import com.example.Excermol.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@DisplayName("PipelineServiceImplTest Unit Tests")
public class PipelineServiceImplTest {

    @Mock
    private PipelineRepository pipelineRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PipelineMapper pipelineMapper;

    @InjectMocks
    private PipelineServiceImpl pipelineService;

    private Pipeline pipeline;
    private PipelineRequestDTO requestDTO;
    private PipelineResponseDTO responseDTO;
    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(10L);
        company.setCompanyName("Test Company");

        pipeline = new Pipeline();
        pipeline.setId(1L);
        pipeline.setName("Test Pipeline");
        pipeline.setStatus(PipelineStatus.AGENCY);
        pipeline.setStage(PipelineStage.WARM);

        requestDTO = new PipelineRequestDTO();
        requestDTO.setName("Test Pipeline");
        requestDTO.setStatus(PipelineStatus.AGENCY);
        requestDTO.setStage(PipelineStage.WARM);
        requestDTO.setCompanyId(10L);

        responseDTO = new PipelineResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Test Pipeline");
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void create_shouldSaveAndReturnPipeline_withoutAssignees() {
        when(companyRepository.findById(10L)).thenReturn(Optional.of(company));
        when(pipelineMapper.toEntity(requestDTO)).thenReturn(pipeline);
        when(pipelineRepository.save(pipeline)).thenReturn(pipeline);
        when(pipelineMapper.toResponseDTO(pipeline)).thenReturn(responseDTO);

        PipelineResponseDTO result = pipelineService.create(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Pipeline");
        assertThat(pipeline.getCompany()).isEqualTo(company);
        assertThat(pipeline.getAssignees()).isEmpty();
        verify(pipelineRepository).save(pipeline);
        verify(userRepository, never()).findAllById(any());
    }

    @Test
    void create_shouldThrowException_whenCompanyNotFound() {
        when(companyRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> pipelineService.create(requestDTO));
        verify(pipelineRepository, never()).save(any());
    }

    @Test
    void create_shouldSetAssignees_whenAssigneeIdsProvided() {
        requestDTO.setAssigneeIds(List.of(20L, 21L));
        User u1 = new User();
        u1.setId(20L);
        User u2 = new User();
        u2.setId(21L);

        when(companyRepository.findById(10L)).thenReturn(Optional.of(company));
        when(userRepository.findAllById(requestDTO.getAssigneeIds())).thenReturn(List.of(u1, u2));
        when(pipelineMapper.toEntity(requestDTO)).thenReturn(pipeline);
        when(pipelineRepository.save(pipeline)).thenReturn(pipeline);
        when(pipelineMapper.toResponseDTO(pipeline)).thenReturn(responseDTO);

        pipelineService.create(requestDTO);

        assertThat(pipeline.getAssignees()).containsExactlyInAnyOrder(u1, u2);
        verify(userRepository).findAllById(requestDTO.getAssigneeIds());
    }

    @Test
    void create_shouldNotCallUserRepository_whenAssigneeIdsNull() {
        when(companyRepository.findById(10L)).thenReturn(Optional.of(company));
        when(pipelineMapper.toEntity(requestDTO)).thenReturn(pipeline);
        when(pipelineRepository.save(pipeline)).thenReturn(pipeline);
        when(pipelineMapper.toResponseDTO(pipeline)).thenReturn(responseDTO);

        pipelineService.create(requestDTO);

        verify(userRepository, never()).findAllById(any());
        assertThat(pipeline.getAssignees()).isEmpty();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getById_shouldReturnPipeline_whenExists() {
        when(pipelineRepository.findById(1L)).thenReturn(Optional.of(pipeline));
        when(pipelineMapper.toResponseDTO(pipeline)).thenReturn(responseDTO);

        PipelineResponseDTO result = pipelineService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(pipelineRepository).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(pipelineRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PipelineNotFoundException.class, () -> pipelineService.getById(99L));
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void update_shouldUpdateAndReturnPipeline() {
        requestDTO.setName("Updated Pipeline");

        when(pipelineRepository.findById(1L)).thenReturn(Optional.of(pipeline));
        when(companyRepository.findById(10L)).thenReturn(Optional.of(company));
        when(pipelineRepository.save(pipeline)).thenReturn(pipeline);
        when(pipelineMapper.toResponseDTO(pipeline)).thenReturn(responseDTO);

        PipelineResponseDTO result = pipelineService.update(1L, requestDTO);

        assertThat(result).isNotNull();
        assertThat(pipeline.getCompany()).isEqualTo(company);
        verify(pipelineMapper).updateEntity(pipeline, requestDTO);
        verify(pipelineRepository).save(pipeline);
    }

    @Test
    void update_shouldThrowException_whenPipelineNotFound() {
        when(pipelineRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PipelineNotFoundException.class, () -> pipelineService.update(99L, requestDTO));
        verify(pipelineRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowException_whenCompanyNotFound() {
        when(pipelineRepository.findById(1L)).thenReturn(Optional.of(pipeline));
        when(companyRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> pipelineService.update(1L, requestDTO));
        verify(pipelineRepository, never()).save(any());
    }

    @Test
    void update_shouldUpdateAssignees_whenAssigneeIdsProvided() {
        requestDTO.setAssigneeIds(List.of(20L));
        User user = new User();
        user.setId(20L);

        when(pipelineRepository.findById(1L)).thenReturn(Optional.of(pipeline));
        when(companyRepository.findById(10L)).thenReturn(Optional.of(company));
        when(userRepository.findAllById(requestDTO.getAssigneeIds())).thenReturn(List.of(user));
        when(pipelineRepository.save(pipeline)).thenReturn(pipeline);
        when(pipelineMapper.toResponseDTO(pipeline)).thenReturn(responseDTO);

        pipelineService.update(1L, requestDTO);

        assertThat(pipeline.getAssignees()).containsExactly(user);
    }

    @Test
    void update_shouldSetEmptyAssignees_whenAssigneeIdsEmpty() {
        requestDTO.setAssigneeIds(List.of());

        when(pipelineRepository.findById(1L)).thenReturn(Optional.of(pipeline));
        when(companyRepository.findById(10L)).thenReturn(Optional.of(company));
        when(pipelineRepository.save(pipeline)).thenReturn(pipeline);
        when(pipelineMapper.toResponseDTO(pipeline)).thenReturn(responseDTO);

        pipelineService.update(1L, requestDTO);

        assertThat(pipeline.getAssignees()).isEmpty();
        verify(userRepository, never()).findAllById(any());
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void delete_shouldDeletePipeline_whenExists() {
        when(pipelineRepository.findById(1L)).thenReturn(Optional.of(pipeline));

        pipelineService.delete(1L);

        verify(pipelineRepository).delete(pipeline);
    }

    @Test
    void delete_shouldThrowException_whenNotFound() {
        when(pipelineRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PipelineNotFoundException.class, () -> pipelineService.delete(99L));
        verify(pipelineRepository, never()).delete(any());
    }

    // =========================
    // GET ALL (Pageable)
    // =========================
    @Test
    void getAll_shouldReturnPagedPipelines() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pipeline> pipelinePage = new PageImpl<>(List.of(pipeline));

        when(pipelineRepository.findAll(pageable)).thenReturn(pipelinePage);
        when(pipelineMapper.toResponseDTO(pipeline)).thenReturn(responseDTO);

        Page<PipelineResponseDTO> result = pipelineService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Test Pipeline");
        verify(pipelineRepository).findAll(pageable);
    }

    @Test
    void getAll_shouldReturnEmptyPage_whenNoPipelines() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pipeline> emptyPage = new PageImpl<>(List.of());

        when(pipelineRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<PipelineResponseDTO> result = pipelineService.getAll(pageable);

        assertThat(result.getContent()).isEmpty();
    }

    // =========================
    // FILTERS
    // =========================
    @Test
    void getByStage_shouldReturnFilteredPipelines() {
        when(pipelineRepository.findByStage(PipelineStage.WARM)).thenReturn(List.of(pipeline));
        when(pipelineMapper.toResponseDTOList(List.of(pipeline))).thenReturn(List.of(responseDTO));

        List<PipelineResponseDTO> result = pipelineService.getByStage(PipelineStage.WARM);

        assertThat(result).hasSize(1);
        verify(pipelineRepository).findByStage(PipelineStage.WARM);
    }

    @Test
    void getByStatus_shouldReturnFilteredPipelines() {
        when(pipelineRepository.findByStatus(PipelineStatus.AGENCY)).thenReturn(List.of(pipeline));
        when(pipelineMapper.toResponseDTOList(List.of(pipeline))).thenReturn(List.of(responseDTO));

        List<PipelineResponseDTO> result = pipelineService.getByStatus(PipelineStatus.AGENCY);

        assertThat(result).hasSize(1);
        verify(pipelineRepository).findByStatus(PipelineStatus.AGENCY);
    }

    @Test
    void getByCompanyId_shouldReturnFilteredPipelines() {
        when(pipelineRepository.findByCompanyId(10L)).thenReturn(List.of(pipeline));
        when(pipelineMapper.toResponseDTOList(List.of(pipeline))).thenReturn(List.of(responseDTO));

        List<PipelineResponseDTO> result = pipelineService.getByCompanyId(10L);

        assertThat(result).hasSize(1);
        verify(pipelineRepository).findByCompanyId(10L);
    }

    @Test
    void getByDateBetween_shouldReturnFilteredPipelines() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 12, 31);

        when(pipelineRepository.findByDateBetween(start, end)).thenReturn(List.of(pipeline));
        when(pipelineMapper.toResponseDTOList(List.of(pipeline))).thenReturn(List.of(responseDTO));

        List<PipelineResponseDTO> result = pipelineService.getByDateBetween(start, end);

        assertThat(result).hasSize(1);
        verify(pipelineRepository).findByDateBetween(start, end);
    }

    @Test
    void getByAssigneeId_shouldReturnFilteredPipelines() {
        when(pipelineRepository.findByAssigneesId(20L)).thenReturn(List.of(pipeline));
        when(pipelineMapper.toResponseDTOList(List.of(pipeline))).thenReturn(List.of(responseDTO));

        List<PipelineResponseDTO> result = pipelineService.getByAssigneeId(20L);

        assertThat(result).hasSize(1);
        verify(pipelineRepository).findByAssigneesId(20L);
    }
}
