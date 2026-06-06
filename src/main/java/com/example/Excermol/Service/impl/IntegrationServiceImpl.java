package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.IntegrationService;
import com.example.Excermol.entity.Integration;
import com.example.Excermol.entity.Workspace;
import com.example.Excermol.entity.dtos.IntegrationCreateRequestDTO;
import com.example.Excermol.entity.dtos.IntegrationResponseDTO;
import com.example.Excermol.entity.dtos.IntegrationUpdateRequestDTO;
import com.example.Excermol.enums.IntegrationType;
import com.example.Excermol.exception.IntegrationAlreadyExistsException;
import com.example.Excermol.exception.IntegrationNotFoundException;
import com.example.Excermol.exception.WorkspaceNotFoundException;
import com.example.Excermol.mapper.IntegrationMapper;
import com.example.Excermol.repository.IntegrationRepository;
import com.example.Excermol.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IntegrationServiceImpl implements IntegrationService {

    private final IntegrationRepository integrationRepository;
    private final WorkspaceRepository workspaceRepository;
    private final IntegrationMapper integrationMapper;

    public IntegrationServiceImpl(IntegrationRepository integrationRepository,
                                  WorkspaceRepository workspaceRepository,
                                  IntegrationMapper integrationMapper) {
        this.integrationRepository = integrationRepository;
        this.workspaceRepository = workspaceRepository;
        this.integrationMapper = integrationMapper;
    }

    @Override
    public IntegrationResponseDTO createIntegration(IntegrationCreateRequestDTO dto) {
        // workspace tap
        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceId())
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace tapılmadı: " + dto.getWorkspaceId()));

        // bu workspace-də eyni tipli integration artıq varmı?
        integrationRepository.findByWorkspaceIdAndIntegrationType(
                        dto.getWorkspaceId(), dto.getIntegrationType())
                .ifPresent(i -> {
                    throw new IntegrationAlreadyExistsException(dto.getIntegrationType() + " artıq mövcuddur");
                });

        Integration integration = integrationMapper.toEntity(dto);
        integration.setWorkspace(workspace);

        Integration saved = integrationRepository.save(integration);
        return integrationMapper.toResponseDTO(saved);
    }

    @Override
    public List<IntegrationResponseDTO> getIntegrationsByWorkspaceId(Long workspaceId) {
        return integrationRepository.findByWorkspaceId(workspaceId)
                .stream()
                .map(integrationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IntegrationResponseDTO getIntegrationById(Long id) {
        Integration integration = integrationRepository.findById(id)
                .orElseThrow(() -> new IntegrationNotFoundException("Integration tapılmadı: " + id));
        return integrationMapper.toResponseDTO(integration);
    }

    @Override
    public IntegrationResponseDTO getIntegrationByType(Long workspaceId, IntegrationType type) {
        Integration integration = integrationRepository
                .findByWorkspaceIdAndIntegrationType(workspaceId, type)
                .orElseThrow(() -> new IntegrationNotFoundException(type + " integration tapılmadı"));
        return integrationMapper.toResponseDTO(integration);
    }

    @Override
    public List<IntegrationResponseDTO> getActiveIntegrations(Long workspaceId) {
        return integrationRepository.findByWorkspaceIdAndIsActive(workspaceId, true)
                .stream()
                .map(integrationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IntegrationResponseDTO updateIntegration(Long id, IntegrationUpdateRequestDTO dto) {
        Integration integration = integrationRepository.findById(id)
                .orElseThrow(() -> new IntegrationNotFoundException("Integration tapılmadı: " + id));

        integrationMapper.updateEntity(integration, dto);

        Integration updated = integrationRepository.save(integration);
        return integrationMapper.toResponseDTO(updated);
    }

    @Override
    public IntegrationResponseDTO toggleIntegration(Long id) {
        Integration integration = integrationRepository.findById(id)
                .orElseThrow(() -> new IntegrationNotFoundException("Integration tapılmadı: " + id));

        // aktiv ↔ deaktiv
        integration.setIsActive(!integration.getIsActive());

        Integration updated = integrationRepository.save(integration);
        return integrationMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteIntegration(Long id) {
        Integration integration = integrationRepository.findById(id)
                .orElseThrow(() -> new IntegrationNotFoundException("Integration tapılmadı: " + id));
        integrationRepository.delete(integration);
    }
}