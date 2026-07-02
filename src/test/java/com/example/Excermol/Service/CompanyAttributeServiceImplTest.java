package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.CompanyAttributeServiceImpl;
import com.example.Excermol.entity.CompanyAttribute;
import com.example.Excermol.entity.Workspace;
import com.example.Excermol.entity.dtos.CompanyAttributeCreateRequestDTO;
import com.example.Excermol.entity.dtos.CompanyAttributeResponseDTO;
import com.example.Excermol.entity.dtos.CompanyAttributeUpdateRequestDTO;
import com.example.Excermol.enums.AttributeProperty;
import com.example.Excermol.enums.AttributeType;
import com.example.Excermol.exception.CompanyAttributeNotFoundException;
import com.example.Excermol.exception.SystemAttributeCannotBeModifiedException;
import com.example.Excermol.exception.WorkspaceNotFoundException;
import com.example.Excermol.mapper.CompanyAttributeMapper;
import com.example.Excermol.repository.CompanyAttributeRepository;
import com.example.Excermol.repository.WorkspaceRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@DisplayName("CompanyAttributeServiceImplTest Unit Tests")
public class CompanyAttributeServiceImplTest {
    @Mock
    private CompanyAttributeRepository companyAttributeRepository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private CompanyAttributeMapper companyAttributeMapper;

    @InjectMocks
    private CompanyAttributeServiceImpl companyAttributeService;

    private Workspace workspace;
    private CompanyAttribute customAttribute;
    private CompanyAttribute systemAttribute;
    private CompanyAttributeCreateRequestDTO createDTO;
    private CompanyAttributeUpdateRequestDTO updateDTO;
    private CompanyAttributeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        workspace = new Workspace();
        workspace.setId(1L);

        customAttribute = new CompanyAttribute();
        customAttribute.setId(1L);
        customAttribute.setName("LinkedIn Company URL");
        customAttribute.setAttributeType(AttributeType.LINKEDIN);
        customAttribute.setAttributeProperty(AttributeProperty.CUSTOM);
        customAttribute.setWorkspace(workspace);

        systemAttribute = new CompanyAttribute();
        systemAttribute.setId(2L);
        systemAttribute.setName("Email");
        systemAttribute.setAttributeType(AttributeType.EMAIL);
        systemAttribute.setAttributeProperty(AttributeProperty.SYSTEM);
        systemAttribute.setWorkspace(workspace);

        createDTO = new CompanyAttributeCreateRequestDTO();
        createDTO.setName("LinkedIn Company URL");
        createDTO.setAttributeType(AttributeType.LINKEDIN);
        createDTO.setAttributeProperty(AttributeProperty.CUSTOM);
        createDTO.setWorkspaceId(1L);

        updateDTO = new CompanyAttributeUpdateRequestDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setAttributeType(AttributeType.TEXT);

        responseDTO = new CompanyAttributeResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("LinkedIn Company URL");
        responseDTO.setAttributeType(AttributeType.LINKEDIN);
        responseDTO.setAttributeProperty(AttributeProperty.CUSTOM);
        responseDTO.setWorkspaceId(1L);
    }

    // ---------- createAttribute ----------

    @Test
    void createAttribute_success() {
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(companyAttributeMapper.toEntity(createDTO)).thenReturn(customAttribute);
        when(companyAttributeRepository.save(customAttribute)).thenReturn(customAttribute);
        when(companyAttributeMapper.toResponseDTO(customAttribute)).thenReturn(responseDTO);

        CompanyAttributeResponseDTO result = companyAttributeService.createAttribute(createDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getName(), result.getName());
        verify(companyAttributeRepository, times(1)).save(customAttribute);
    }

    @Test
    void createAttribute_workspaceNotFound_throwsException() {
        when(workspaceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkspaceNotFoundException.class,
                () -> companyAttributeService.createAttribute(createDTO));

        verify(companyAttributeRepository, never()).save(any());
    }

    // ---------- getAttributesByWorkspaceId ----------

    @Test
    void getAttributesByWorkspaceId_success() {
        when(companyAttributeRepository.findAllByWorkspaceId(1L))
                .thenReturn(Arrays.asList(customAttribute, systemAttribute));
        when(companyAttributeMapper.toResponseDTO(any(CompanyAttribute.class))).thenReturn(responseDTO);

        List<CompanyAttributeResponseDTO> result = companyAttributeService.getAttributesByWorkspaceId(1L);

        assertEquals(2, result.size());
        verify(companyAttributeRepository, times(1)).findAllByWorkspaceId(1L);
    }

    @Test
    void getAttributesByWorkspaceId_emptyList() {
        when(companyAttributeRepository.findAllByWorkspaceId(1L)).thenReturn(List.of());

        List<CompanyAttributeResponseDTO> result = companyAttributeService.getAttributesByWorkspaceId(1L);

        assertTrue(result.isEmpty());
        verify(companyAttributeMapper, never()).toResponseDTO(any());
    }

    // ---------- getAttributeById ----------

    @Test
    void getAttributeById_success() {
        when(companyAttributeRepository.findById(1L)).thenReturn(Optional.of(customAttribute));
        when(companyAttributeMapper.toResponseDTO(customAttribute)).thenReturn(responseDTO);

        CompanyAttributeResponseDTO result = companyAttributeService.getAttributeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getAttributeById_notFound_throwsException() {
        when(companyAttributeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CompanyAttributeNotFoundException.class,
                () -> companyAttributeService.getAttributeById(1L));
    }

    // ---------- getAttributesByProperty ----------

    @Test
    void getAttributesByProperty_success() {
        when(companyAttributeRepository.findAllByWorkspaceIdAndAttributeProperty(1L, AttributeProperty.CUSTOM))
                .thenReturn(List.of(customAttribute));
        when(companyAttributeMapper.toResponseDTO(customAttribute)).thenReturn(responseDTO);

        List<CompanyAttributeResponseDTO> result =
                companyAttributeService.getAttributesByProperty(1L, AttributeProperty.CUSTOM);

        assertEquals(1, result.size());
        verify(companyAttributeRepository, times(1))
                .findAllByWorkspaceIdAndAttributeProperty(1L, AttributeProperty.CUSTOM);
    }

    @Test
    void getAttributesByProperty_emptyList() {
        when(companyAttributeRepository.findAllByWorkspaceIdAndAttributeProperty(1L, AttributeProperty.SYSTEM))
                .thenReturn(List.of());

        List<CompanyAttributeResponseDTO> result =
                companyAttributeService.getAttributesByProperty(1L, AttributeProperty.SYSTEM);

        assertTrue(result.isEmpty());
    }

    // ---------- getAttributesByType ----------

    @Test
    void getAttributesByType_success() {
        when(companyAttributeRepository.findAllByWorkspaceIdAndAttributeType(1L, AttributeType.LINKEDIN))
                .thenReturn(List.of(customAttribute));
        when(companyAttributeMapper.toResponseDTO(customAttribute)).thenReturn(responseDTO);

        List<CompanyAttributeResponseDTO> result =
                companyAttributeService.getAttributesByType(1L, AttributeType.LINKEDIN);

        assertEquals(1, result.size());
        verify(companyAttributeRepository, times(1))
                .findAllByWorkspaceIdAndAttributeType(1L, AttributeType.LINKEDIN);
    }

    @Test
    void getAttributesByType_emptyList() {
        when(companyAttributeRepository.findAllByWorkspaceIdAndAttributeType(1L, AttributeType.WHATSAPP))
                .thenReturn(List.of());

        List<CompanyAttributeResponseDTO> result =
                companyAttributeService.getAttributesByType(1L, AttributeType.WHATSAPP);

        assertTrue(result.isEmpty());
    }

    // ---------- searchAttributesByName ----------

    @Test
    void searchAttributesByName_success() {
        when(companyAttributeRepository.findAllByWorkspaceIdAndNameContainingIgnoreCase(1L, "linkedin"))
                .thenReturn(List.of(customAttribute));
        when(companyAttributeMapper.toResponseDTO(customAttribute)).thenReturn(responseDTO);

        List<CompanyAttributeResponseDTO> result =
                companyAttributeService.searchAttributesByName(1L, "linkedin");

        assertEquals(1, result.size());
        verify(companyAttributeRepository, times(1))
                .findAllByWorkspaceIdAndNameContainingIgnoreCase(1L, "linkedin");
    }

    @Test
    void searchAttributesByName_noMatch_emptyList() {
        when(companyAttributeRepository.findAllByWorkspaceIdAndNameContainingIgnoreCase(1L, "xyz"))
                .thenReturn(List.of());

        List<CompanyAttributeResponseDTO> result =
                companyAttributeService.searchAttributesByName(1L, "xyz");

        assertTrue(result.isEmpty());
    }

    // ---------- updateAttribute ----------

    @Test
    void updateAttribute_success() {
        when(companyAttributeRepository.findById(1L)).thenReturn(Optional.of(customAttribute));
        when(companyAttributeRepository.save(customAttribute)).thenReturn(customAttribute);
        when(companyAttributeMapper.toResponseDTO(customAttribute)).thenReturn(responseDTO);

        CompanyAttributeResponseDTO result = companyAttributeService.updateAttribute(1L, updateDTO);

        assertNotNull(result);
        verify(companyAttributeMapper, times(1)).updateEntity(customAttribute, updateDTO);
        verify(companyAttributeRepository, times(1)).save(customAttribute);
    }

    @Test
    void updateAttribute_notFound_throwsException() {
        when(companyAttributeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CompanyAttributeNotFoundException.class,
                () -> companyAttributeService.updateAttribute(1L, updateDTO));

        verify(companyAttributeRepository, never()).save(any());
    }

    @Test
    void updateAttribute_systemAttribute_throwsException() {
        when(companyAttributeRepository.findById(2L)).thenReturn(Optional.of(systemAttribute));

        assertThrows(SystemAttributeCannotBeModifiedException.class,
                () -> companyAttributeService.updateAttribute(2L, updateDTO));

        verify(companyAttributeMapper, never()).updateEntity(any(), any());
        verify(companyAttributeRepository, never()).save(any());
    }

    // ---------- deleteAttribute ----------

    @Test
    void deleteAttribute_success() {
        when(companyAttributeRepository.findById(1L)).thenReturn(Optional.of(customAttribute));
        doNothing().when(companyAttributeRepository).delete(customAttribute);

        companyAttributeService.deleteAttribute(1L);

        verify(companyAttributeRepository, times(1)).delete(customAttribute);
    }

    @Test
    void deleteAttribute_notFound_throwsException() {
        when(companyAttributeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CompanyAttributeNotFoundException.class,
                () -> companyAttributeService.deleteAttribute(1L));

        verify(companyAttributeRepository, never()).delete(any());
    }

    @Test
    void deleteAttribute_systemAttribute_throwsException() {
        when(companyAttributeRepository.findById(2L)).thenReturn(Optional.of(systemAttribute));

        assertThrows(SystemAttributeCannotBeModifiedException.class,
                () -> companyAttributeService.deleteAttribute(2L));

        verify(companyAttributeRepository, never()).delete(any());
    }

}
