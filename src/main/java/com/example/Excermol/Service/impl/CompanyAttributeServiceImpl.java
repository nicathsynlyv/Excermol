package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.CompanyAttributeService;
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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyAttributeServiceImpl implements CompanyAttributeService {

    private final CompanyAttributeRepository companyAttributeRepository;
    private final WorkspaceRepository workspaceRepository;
    private final CompanyAttributeMapper companyAttributeMapper;

    public CompanyAttributeServiceImpl(CompanyAttributeRepository companyAttributeRepository,
                                       WorkspaceRepository workspaceRepository,
                                       CompanyAttributeMapper companyAttributeMapper) {
        this.companyAttributeRepository = companyAttributeRepository;
        this.workspaceRepository = workspaceRepository;
        this.companyAttributeMapper = companyAttributeMapper;
    }

    @Override
    public CompanyAttributeResponseDTO createAttribute(CompanyAttributeCreateRequestDTO dto) {
        // workspace tap
        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceId())
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace tapılmadı: " + dto.getWorkspaceId()));

        CompanyAttribute attribute = companyAttributeMapper.toEntity(dto);
        attribute.setWorkspace(workspace);

        CompanyAttribute saved = companyAttributeRepository.save(attribute);
        return companyAttributeMapper.toResponseDTO(saved);
    }

    @Override
    public List<CompanyAttributeResponseDTO> getAttributesByWorkspaceId(Long workspaceId) {
        return companyAttributeRepository.findAllByWorkspaceId(workspaceId)
                .stream()
                .map(companyAttributeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyAttributeResponseDTO getAttributeById(Long id) {
        CompanyAttribute attribute = companyAttributeRepository.findById(id)
                .orElseThrow(() -> new CompanyAttributeNotFoundException("Attribute tapılmadı: " + id));
        return companyAttributeMapper.toResponseDTO(attribute);
    }

    @Override
    public List<CompanyAttributeResponseDTO> getAttributesByProperty(Long workspaceId, AttributeProperty property) {
        return companyAttributeRepository.findAllByWorkspaceIdAndAttributeProperty(workspaceId, property)
                .stream()
                .map(companyAttributeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyAttributeResponseDTO> getAttributesByType(Long workspaceId, AttributeType type) {
        return companyAttributeRepository.findAllByWorkspaceIdAndAttributeType(workspaceId, type)
                .stream()
                .map(companyAttributeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyAttributeResponseDTO> searchAttributesByName(Long workspaceId, String name) {
        return companyAttributeRepository.findAllByWorkspaceIdAndNameContainingIgnoreCase(workspaceId, name)
                .stream()
                .map(companyAttributeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyAttributeResponseDTO updateAttribute(Long id, CompanyAttributeUpdateRequestDTO dto) {
        CompanyAttribute attribute = companyAttributeRepository.findById(id)
                .orElseThrow(() -> new CompanyAttributeNotFoundException("Attribute tapılmadı: " + id));

        // SYSTEM attribute dəyişdirilə bilməz
        if (attribute.getAttributeProperty() == AttributeProperty.SYSTEM) {
            throw new SystemAttributeCannotBeModifiedException("SYSTEM attribute dəyişdirilə bilməz: " + attribute.getName());
        }

        companyAttributeMapper.updateEntity(attribute, dto);

        CompanyAttribute updated = companyAttributeRepository.save(attribute);
        return companyAttributeMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteAttribute(Long id) {
        CompanyAttribute attribute = companyAttributeRepository.findById(id)
                .orElseThrow(() -> new CompanyAttributeNotFoundException("Attribute tapılmadı: " + id));

        // SYSTEM attribute silinə bilməz
        if (attribute.getAttributeProperty() == AttributeProperty.SYSTEM) {
            throw new SystemAttributeCannotBeModifiedException("SYSTEM attribute silinə bilməz: " + attribute.getName());
        }

        companyAttributeRepository.delete(attribute);
    }
}
