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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
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
//1
    @Override
    public IntegrationResponseDTO createIntegration(IntegrationCreateRequestDTO dto) {
        log.info("Creating Integration for workspace id: {} with type: {}", dto.getWorkspaceId(), dto.getIntegrationType());
        // workspace tap
        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceId())
                .orElseThrow(() -> {
                    log.warn("Workspace not found with id: {}", dto.getWorkspaceId());
                    return new WorkspaceNotFoundException("Workspace tapılmadı: " + dto.getWorkspaceId());
                });
        // bu workspace-də eyni tipli integration artıq varmı?
        integrationRepository.findByWorkspaceIdAndIntegrationType(dto.getWorkspaceId(), dto.getIntegrationType())
                .ifPresent(i -> {
                    log.warn("Integration already exists for workspace id: {} with type: {}", dto.getWorkspaceId(), dto.getIntegrationType());
                    throw new IntegrationAlreadyExistsException(dto.getIntegrationType() + " artıq mövcuddur");
                });

        Integration integration = integrationMapper.toEntity(dto);
        integration.setWorkspace(workspace);

        Integration saved = integrationRepository.save(integration);
        log.info("Integration created successfully with id: {}", saved.getId());
        return integrationMapper.toResponseDTO(saved);
    }
//2
    @Override
    public List<IntegrationResponseDTO> getIntegrationsByWorkspaceId(Long workspaceId) {
        log.info("Fetching integrations for workspace id: {}", workspaceId);
        List<IntegrationResponseDTO> integrations = integrationRepository.findByWorkspaceId(workspaceId)
                .stream()
                .map(integrationMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.info("Retrieved {} integrations for workspace id: {}", integrations.size(), workspaceId);
        return integrations;
    }
//3
    @Override
    public IntegrationResponseDTO getIntegrationById(Long id) {
        log.info("Fetching integration with id: {}", id);
        Integration integration = integrationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Integration not found with id: {}", id);
                    return new IntegrationNotFoundException("Integration tapılmadı: " + id);
                });
        log.info("Integration found with id: {}", id);
        return integrationMapper.toResponseDTO(integration);
    }
//4
    @Override
    public IntegrationResponseDTO getIntegrationByType(Long workspaceId, IntegrationType type) {
        log.info("Fetching integration for workspace id: {} with type: {}", workspaceId, type);
        Integration integration = integrationRepository
                .findByWorkspaceIdAndIntegrationType(workspaceId, type)
                .orElseThrow(() -> {
                    log.warn("Integration not found for workspace id: {} with type: {}", workspaceId, type);
                    return new IntegrationNotFoundException(type + " integration tapılmadı");
                });

        log.info("Integration found for workspace id: {} with type: {}", workspaceId, type);
        return integrationMapper.toResponseDTO(integration);
    }
//5
    @Override
    public List<IntegrationResponseDTO> getActiveIntegrations(Long workspaceId) {
        log.info("Fetching active integrations for workspace id: {}", workspaceId);
        List<IntegrationResponseDTO> integrations = integrationRepository.findByWorkspaceIdAndIsActive(workspaceId, true)
                .stream()
                .map(integrationMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} active integrations for workspace id: {}", integrations.size(), workspaceId);
        return integrations;
    }
//6
    @Override
    public IntegrationResponseDTO updateIntegration(Long id, IntegrationUpdateRequestDTO dto) {
        log.info("Updating integration with id: {}", id);
        Integration integration = integrationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Integration not found for update. Id: {}", id);
                    return new IntegrationNotFoundException("Integration tapılmadı: " + id);
                });
        integrationMapper.updateEntity(integration, dto);

        Integration updated = integrationRepository.save(integration);
        log.info("Integration updated successfully. Id: {}", id);
        return integrationMapper.toResponseDTO(updated);
    }
//7
    @Override
    public IntegrationResponseDTO toggleIntegration(Long id) {
        log.info("Toggling integration with id: {}", id);
        Integration integration = integrationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Integration not found with id: {}", id);
                    return new IntegrationNotFoundException("Integration tapılmadı: " + id);
                });
        // aktiv ↔ deaktiv
        integration.setIsActive(!integration.getIsActive());

        Integration updated = integrationRepository.save(integration);
        log.info("Integration toggled to isActive={} for id: {}", updated.getIsActive(), id);
        return integrationMapper.toResponseDTO(updated);
    }
//8
    @Override
    public void deleteIntegration(Long id) {
        log.info("Deleting integration with id: {}", id);
        Integration integration = integrationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Integration not found for deletion. Id: {}", id);
                    return new IntegrationNotFoundException("Integration tapılmadı: " + id);
                });
        integrationRepository.delete(integration);
        log.info("Integration deleted successfully. Id: {}", id);
    }
}