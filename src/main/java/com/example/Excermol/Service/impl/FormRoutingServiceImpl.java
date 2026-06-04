package com.example.Excermol.Service.impl;


import com.example.Excermol.Service.FormRoutingService;
import com.example.Excermol.entity.Email;
import com.example.Excermol.entity.Form;
import com.example.Excermol.entity.FormField;
import com.example.Excermol.entity.FormRouting;
import com.example.Excermol.entity.dtos.FormRoutingCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormRoutingResponseDTO;
import com.example.Excermol.entity.dtos.FormRoutingUpdateRequestDTO;
import com.example.Excermol.exception.FormFieldNotFoundException;
import com.example.Excermol.exception.FormNotFoundException;
import com.example.Excermol.exception.FormRoutingNotFoundException;
import com.example.Excermol.mapper.FormRoutingMapper;
import com.example.Excermol.repository.EmailRepository;
import com.example.Excermol.repository.FormFieldRepository;
import com.example.Excermol.repository.FormRepository;
import com.example.Excermol.repository.FormRoutingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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
        // form tap
        Form form = formRepository.findById(dto.getFormId())
                .orElseThrow(() -> new FormNotFoundException("Form tapılmadı: " + dto.getFormId()));

        // formField tap
        FormField formField = formFieldRepository.findById(dto.getFormFieldId())
                .orElseThrow(() -> new FormFieldNotFoundException("FormField tapılmadı: " + dto.getFormFieldId()));

        // routing yarat
        FormRouting formRouting = formRoutingMapper.toEntity(dto);
        formRouting.setForm(form);
        formRouting.setFormField(formField);

        // email set et (optional)
        if (dto.getEmailId() != null) {
            Email email = emailRepository.findById(dto.getEmailId())
                    .orElseThrow(() -> new RuntimeException("Email tapılmadı: " + dto.getEmailId()));
            formRouting.setEmail(email);
        }

        FormRouting saved = formRoutingRepository.save(formRouting);
        return formRoutingMapper.toResponseDTO(saved);
    }

    @Override
    public List<FormRoutingResponseDTO> getRoutingsByFormId(Long formId) {
        return formRoutingRepository.findAllByFormIdOrderByRoutingOrderAsc(formId)
                .stream()
                .map(formRoutingMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FormRoutingResponseDTO getRoutingById(Long id) {
        FormRouting formRouting = formRoutingRepository.findById(id)
                .orElseThrow(() -> new FormRoutingNotFoundException("FormRouting tapılmadı: " + id));
        return formRoutingMapper.toResponseDTO(formRouting);
    }

    @Override
    public FormRoutingResponseDTO updateRouting(Long id, FormRoutingUpdateRequestDTO dto) {
        FormRouting formRouting = formRoutingRepository.findById(id)
                .orElseThrow(() -> new FormRoutingNotFoundException("FormRouting tapılmadı: " + id));

        formRoutingMapper.updateEntity(formRouting, dto);

        // formField update et (optional)
        if (dto.getFormFieldId() != null) {
            FormField formField = formFieldRepository.findById(dto.getFormFieldId())
                    .orElseThrow(() -> new FormFieldNotFoundException("FormField tapılmadı: " + dto.getFormFieldId()));
            formRouting.setFormField(formField);
        }

        // email update et (optional)
        if (dto.getEmailId() != null) {
            Email email = emailRepository.findById(dto.getEmailId())
                    .orElseThrow(() -> new RuntimeException("Email tapılmadı: " + dto.getEmailId()));
            formRouting.setEmail(email);
        }

        FormRouting updated = formRoutingRepository.save(formRouting);
        return formRoutingMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteRouting(Long id) {
        FormRouting formRouting = formRoutingRepository.findById(id)
                .orElseThrow(() -> new FormRoutingNotFoundException("FormRouting tapılmadı: " + id));
        formRoutingRepository.delete(formRouting);
    }
}

