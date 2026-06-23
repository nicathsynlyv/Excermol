package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.FormService;
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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class FormServiceImpl implements FormService {

    private final FormRepository formRepository;
    private final UserRepository userRepository;
    private final FormMapper formMapper;

    public FormServiceImpl(FormRepository formRepository,
                           UserRepository userRepository,
                           FormMapper formMapper) {
        this.formRepository = formRepository;
        this.userRepository = userRepository;
        this.formMapper = formMapper;
    }

    @Override
    public FormResponseDTO createForm(FormCreateRequestDTO dto, Long ownerId) {
        log.info("Creating form for owner id: {}", ownerId);
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", ownerId);
                    return new UserNotFoundException("User tapılmadı: " + ownerId);
                });

        Form form = formMapper.toEntity(dto);
        form.setOwner(owner);

        Form saved = formRepository.save(form);
        log.info("Form created successfully with id: {}", saved.getId());
        return formMapper.toResponseDTO(saved);
    }

    @Override
    public List<FormResponseDTO> getAllForms() {
        log.info("Fetching all forms");
        List<FormResponseDTO> forms = formRepository.findAll()
                .stream()
                .map(formMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.info("Retrieved {} forms", forms.size());
        return forms;
    }

    @Override
    public FormResponseDTO getFormById(Long id) {
        log.info("Fetching form with id: {}", id);
        Form form = formRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Form not found with id: {}", id);
                    return new FormNotFoundException("Form tapılmadı: " + id);
                });

        return formMapper.toResponseDTO(form);
    }

    @Override
    public List<FormResponseDTO> getFormsByOwner(Long ownerId) {
        log.info("Fetching forms for ownerId: {}", ownerId);

        List<FormResponseDTO> forms = formRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(formMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} forms for owner id: {}", forms.size(),ownerId);
        return forms;
    }

    @Override
    public FormResponseDTO updateForm(Long id, FormUpdateRequestDTO dto) {
        log.info("Updating form with id: {}", id);
        Form form = formRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Form not found for update. Id: {}", id);
                    return new FormNotFoundException("Form tapılmadı: " + id);
                        });

        formMapper.updateEntity(form, dto);

        Form updated = formRepository.save(form);
        log.info("Form updated successfully. Id: {}", id);
        return formMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteForm(Long id) {
        log.info("Deleting form with id: {}", id);
        Form form = formRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Form not found for delete. Id: {}", id);
                    return new FormNotFoundException("Form tapılmadı: " + id);
                });
        formRepository.delete(form);
        log.info("Form deleted successfully. Id: {}", id);
    }


    @Override
    public FormResponseDTO toggleFormStatus(Long id) {
        log.info("Toggling status for form with id: {}", id);
        Form form = formRepository.findById(id)
                .orElseThrow(()->{
                    log.warn("Form not found with Id: {}", id);
                    return new FormNotFoundException("Form tapılmadı: " + id);
                });
        if (form.getStatus() == FormStatus.PUBLISHED) {
            form.setStatus(FormStatus.DRAFT);
        } else {
            form.setStatus(FormStatus.PUBLISHED);
        }

        Form updated = formRepository.save(form);
        log.info("Form status toggled to {} for id: {}",updated.getStatus(),id);
        return formMapper.toResponseDTO(updated);
    }
}
