package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.FormRoutingService;
import com.example.Excermol.entity.Email;
import com.example.Excermol.entity.Form;
import com.example.Excermol.entity.FormField;
import com.example.Excermol.entity.FormRouting;
import com.example.Excermol.entity.dtos.FormRoutingCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormRoutingResponseDTO;
import com.example.Excermol.entity.dtos.FormRoutingUpdateRequestDTO;
import com.example.Excermol.exception.EmailNotFoundException;
import com.example.Excermol.exception.FormFieldNotFoundException;
import com.example.Excermol.exception.FormNotFoundException;
import com.example.Excermol.exception.FormRoutingNotFoundException;
import com.example.Excermol.mapper.FormRoutingMapper;
import com.example.Excermol.repository.EmailRepository;
import com.example.Excermol.repository.FormFieldRepository;
import com.example.Excermol.repository.FormRepository;
import com.example.Excermol.repository.FormRoutingRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class FormRoutingServiceImpl implements FormRoutingService {

    private final FormRoutingRepository formRoutingRepository;
    private final FormRepository formRepository;
    private final FormFieldRepository formFieldRepository;
    private final EmailRepository emailRepository;
    private final FormRoutingMapper formRoutingMapper;

    public FormRoutingServiceImpl(FormRoutingRepository formRoutingRepository,
                                  FormRepository formRepository,
                                  FormFieldRepository formFieldRepository,
                                  EmailRepository emailRepository,
                                  FormRoutingMapper formRoutingMapper) {
        this.formRoutingRepository = formRoutingRepository;
        this.formRepository = formRepository;
        this.formFieldRepository = formFieldRepository;
        this.emailRepository = emailRepository;
        this.formRoutingMapper = formRoutingMapper;
    }

    @Override
    public FormRoutingResponseDTO createRouting(FormRoutingCreateRequestDTO dto) {
        log.info("Creating Form Routing for Form id: {} ", dto.getFormId());
        // form tap
        Form form = formRepository.findById(dto.getFormId())
                .orElseThrow(() -> {
                    log.warn("Form not found with id: {} ", dto.getFormId());
                    return new FormNotFoundException("Form tapilmadi: " + dto.getFormId());
                });

        // formField tap
        FormField formField = formFieldRepository.findById(dto.getFormFieldId())
                .orElseThrow(() -> {
                    log.warn("Formfield not found with id: {} ", dto.getFormFieldId());
                    return new FormFieldNotFoundException("Formfield tapilmadi: " + dto.getFormFieldId());
                });

        // routing yarat
        FormRouting formRouting = formRoutingMapper.toEntity(dto);
        formRouting.setForm(form);
        formRouting.setFormField(formField);

        // email set et (optional)
        if (dto.getEmailId() != null) {
            log.info("Setting email with id: {} ", dto.getEmailId());
            Email email = emailRepository.findById(dto.getEmailId())
                    .orElseThrow(() -> {
                        log.warn("email not found with id: {} ", dto.getEmailId());
                        return new EmailNotFoundException("Email tapilmadi: " + dto.getEmailId());
                    });
            formRouting.setEmail(email);
        }

        FormRouting saved = formRoutingRepository.save(formRouting);
        log.info("Form routing created successfully with id: {}", saved.getId());
        return formRoutingMapper.toResponseDTO(saved);
    }

    @Override
    public List<FormRoutingResponseDTO> getRoutingsByFormId(Long formId) {
        log.info("Fetching routings for form id: {}", formId);

        List<FormRoutingResponseDTO> routings = formRoutingRepository.findAllByFormIdOrderByRoutingOrderAsc(formId)
                .stream()
                .map(formRoutingMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} routings for form Id: {}", routings.size(), formId);
        return routings;
    }

    @Override
    public FormRoutingResponseDTO getRoutingById(Long id) {
        log.info("Fetching form routing with id: {}", id);
        FormRouting formRouting = formRoutingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("FormRouting not found with id: {} ", id);
                    return new FormRoutingNotFoundException("FormRouting tapilmadi: " + id);
                });
        log.info("Form routing found with id: {}", id);
        return formRoutingMapper.toResponseDTO(formRouting);
    }

    @Override
    public FormRoutingResponseDTO updateRouting(Long id, FormRoutingUpdateRequestDTO dto) {
        log.info("Updating form routing with id: {}", id);
        FormRouting formRouting = formRoutingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Form routing not found with id: {} ", id);
                    return new FormRoutingNotFoundException("FormRouting tapilmadi: " + id);
                });
        formRoutingMapper.updateEntity(formRouting, dto);

        // formField update et (optional)
        if (dto.getFormFieldId() != null) {
            log.info("Updating form field with id: {} ", dto.getFormFieldId());
            FormField formField = formFieldRepository.findById(dto.getFormFieldId())
                    .orElseThrow(() -> {
                        log.warn("Formfield not found with id: {} ", dto.getFormFieldId());
                        return new FormFieldNotFoundException("Formfield tapilmadi: " + dto.getFormFieldId());
                    });
            formRouting.setFormField(formField);
        }

        // email update et (optional)
        if (dto.getEmailId() != null) {
            log.info("Updating email with id: {} ", dto.getEmailId());
            Email email = emailRepository.findById(dto.getEmailId())
                    .orElseThrow(() -> {
                        log.warn("Email not found with id: {} ", dto.getEmailId());
                        return new EmailNotFoundException("Email tapilmadi: " + dto.getEmailId());
                    });
            formRouting.setEmail(email);
        }

        FormRouting updated = formRoutingRepository.save(formRouting);
        log.info("Form routing updated successfully. Id: {}", id);
        return formRoutingMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteRouting(Long id) {
        log.info("Deleting form routing with id: {}", id);

        FormRouting formRouting = formRoutingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("FormRouting not found for deletion. Id: {}", id);
                    return new FormRoutingNotFoundException("FormRouting tapilmadi: " + id);
                });
        formRoutingRepository.delete(formRouting);
        log.info("Form routing deleted successfully. Id: {}", id);
    }
}

