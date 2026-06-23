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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
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
//1
    @Override
    public CompanyAttributeResponseDTO createAttribute(CompanyAttributeCreateRequestDTO dto) {
        log.info("Creating company attribute for workspace id: {}", dto.getWorkspaceId());
        // workspace tap
        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceId())
                .orElseThrow(() ->{
                    log.warn("Workspace Not found with id: {}", dto.getWorkspaceId());
                    return new  WorkspaceNotFoundException("Workspace tapılmadı: " + dto.getWorkspaceId());
                        });
        CompanyAttribute attribute = companyAttributeMapper.toEntity(dto);
        attribute.setWorkspace(workspace);

        CompanyAttribute saved = companyAttributeRepository.save(attribute);
        log.info("Company attribute created successfull with Id: {}", saved.getId());
        return companyAttributeMapper.toResponseDTO(saved);
    }
//2
    @Override
    public List<CompanyAttributeResponseDTO> getAttributesByWorkspaceId(Long workspaceId) {
        log.info("Fetching attributes for workspace id: {}", workspaceId);

        List<CompanyAttributeResponseDTO> attributes = companyAttributeRepository.findAllByWorkspaceId(workspaceId)
                .stream()
                .map(companyAttributeMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.info("Retrieved {} attributes for workspace id: {}", attributes.size(), workspaceId);
        return attributes;
    }
//3
    @Override
    public CompanyAttributeResponseDTO getAttributeById(Long id) {
        log.info("Fetching company attribute with id: {}", id);
        CompanyAttribute attribute = companyAttributeRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("Attribute not found with id: {}", id);
                  return new CompanyAttributeNotFoundException("Attribute tapılmadı: " + id);
                });

        log.info("Company attribute found with Id: {}", attribute);
        return companyAttributeMapper.toResponseDTO(attribute);
    }

//4
    @Override
    public List<CompanyAttributeResponseDTO> getAttributesByProperty(Long workspaceId, AttributeProperty property) {
        log.info("Fetching attributes for workspace id: {} with property: {}", workspaceId, property);


        List<CompanyAttributeResponseDTO> attributes = companyAttributeRepository.findAllByWorkspaceIdAndAttributeProperty(workspaceId, property)
                .stream()
                .map(companyAttributeMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} attributes for workspace id: {} with property id: {}", attributes.size(), workspaceId, property);
        return attributes;
    }
//5
    @Override
    public List<CompanyAttributeResponseDTO> getAttributesByType(Long workspaceId, AttributeType type) {
        log.info("Fetching attributes for workspace id: {} with type: {}", workspaceId, type);


        List<CompanyAttributeResponseDTO> attributes = companyAttributeRepository.findAllByWorkspaceIdAndAttributeType(workspaceId, type)
                .stream()
                .map(companyAttributeMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} attributes for workspace id: {} with type: {}", attributes.size(), workspaceId, type);
        return attributes;
    }
//6
    @Override
    public List<CompanyAttributeResponseDTO> searchAttributesByName(Long workspaceId, String name) {
        log.info("Searching attributes for workspace id: {} with name: {}", workspaceId, name);


        List<CompanyAttributeResponseDTO> attributes = companyAttributeRepository.findAllByWorkspaceIdAndNameContainingIgnoreCase(workspaceId, name)
                .stream()
                .map(companyAttributeMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} attributes for workspace id: {} with  matching name: {}", attributes.size(), workspaceId, name);
        return attributes;
    }
//7
    @Override
    public CompanyAttributeResponseDTO updateAttribute(Long id, CompanyAttributeUpdateRequestDTO dto) {
        log.info("Updating company attribute with id: {}", id);
        CompanyAttribute attribute = companyAttributeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attribute not found for update: {}", id);
                    return new  CompanyAttributeNotFoundException("Attribute tapılmadı: " + id);
                        });
        // SYSTEM attribute dəyişdirilə bilməz
        if (attribute.getAttributeProperty() == AttributeProperty.SYSTEM) {
            log.info("Attempt to modify SYSTEM attribute: {} ", attribute.getName());
            throw new SystemAttributeCannotBeModifiedException("SYSTEM attribute dəyişdirilə bilməz: " + attribute.getName());
        }

        companyAttributeMapper.updateEntity(attribute, dto);

        CompanyAttribute updated = companyAttributeRepository.save(attribute);
        log.info("Company attribute updated successfully. Id: {}", id);
        return companyAttributeMapper.toResponseDTO(updated);
    }
//8
    @Override
    public void deleteAttribute(Long id) {
        log.info("Deleting company attribute with id: {}", id);
        CompanyAttribute attribute = companyAttributeRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("Attribute not found for deletion: {}", id);
                    return new CompanyAttributeNotFoundException("Attribute tapılmadı: " + id);
                        });
        // SYSTEM attribute silinə bilməz
        if (attribute.getAttributeProperty() == AttributeProperty.SYSTEM) {
            log.info("Attempt to delete SYSTEM attribute: {} ", attribute.getName());
            throw new SystemAttributeCannotBeModifiedException("SYSTEM attribute silinə bilməz: " + attribute.getName());
        }

        companyAttributeRepository.delete(attribute);
        log.info("Company attribute deleted successfully. Id: {}", id);
    }
}
