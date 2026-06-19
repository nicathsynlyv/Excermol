package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.FormFieldService;
import com.example.Excermol.entity.Form;
import com.example.Excermol.entity.FormField;
import com.example.Excermol.entity.dtos.FormFieldCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormFieldResponseDTO;
import com.example.Excermol.entity.dtos.FormFieldUpdateRequestDTO;
import com.example.Excermol.exception.FormFieldNotFoundException;
import com.example.Excermol.exception.FormNotFoundException;
import com.example.Excermol.mapper.FormFieldMapper;
import com.example.Excermol.repository.FormFieldRepository;
import com.example.Excermol.repository.FormRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class FormFieldServiceImpl implements FormFieldService {


    private final FormFieldRepository formFieldRepository;
    private final FormRepository formRepository;
    private final FormFieldMapper formFieldMapper;

    public FormFieldServiceImpl(FormFieldRepository formFieldRepository,
                                FormRepository formRepository,
                                FormFieldMapper formFieldMapper) {
        this.formFieldRepository = formFieldRepository;
        this.formRepository = formRepository;
        this.formFieldMapper = formFieldMapper;
    }

    //1
    @Override
    public FormFieldResponseDTO createField(FormFieldCreateRequestDTO dto) {
        log.info("Creating Formfield for form id {}", dto.getFormId());
        Form form = formRepository.findById(dto.getFormId())
                .orElseThrow(() -> {
                    log.warn("Form not found with id: {}", dto.getFormId());
                    return new FormNotFoundException("Form tapilmadi:" + dto.getFormId());
                });
        FormField formField = formFieldMapper.toEntity(dto);
        formField.setForm(form);

        FormField saved = formFieldRepository.save(formField);
        log.info("Form field created successfully with id: {}", saved.getId());
        return formFieldMapper.toResponseDTO(saved);
    }

    //2
    @Override
    public List<FormFieldResponseDTO> getFieldsByFormId(Long formId) {
        log.info("Fetching formfields for form id {}", formId);

        List<FormFieldResponseDTO> fields = formFieldRepository.findAllByFormIdOrderByFieldOrderAsc(formId)
                .stream()
                .map(formFieldMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} formfields for form id {}", fields.size(), formId);
        return fields;
    }

    //3
    @Override
    public FormFieldResponseDTO getFieldById(Long id) {
        log.info("Fetching Form field with id {}", id);
        FormField formField = formFieldRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Formfield not found with id: {}", id);
                    return new FormFieldNotFoundException("Formfield tapilmadi:" + id);
                });
        log.info("Formfield found with id: {}", id);
        return formFieldMapper.toResponseDTO(formField);
    }

    //4
    @Override
    public FormFieldResponseDTO updateField(Long id, FormFieldUpdateRequestDTO dto) {
        log.info("Updating Formfield with id {}", id);
        FormField formField = formFieldRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Formfield not found for update. Id: {}", id);
                    return new FormFieldNotFoundException("Formfield tapilmadi:" + id);
                });
        formFieldMapper.updateEntity(formField, dto);

        FormField updated = formFieldRepository.save(formField);
        log.info("Formfield updated successfully. Id: {}", id);
        return formFieldMapper.toResponseDTO(updated);
    }

    //5
    @Override
    public void deleteField(Long id) {
        log.info("Deleting Formfield with id {}", id);
        FormField formField = formFieldRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Formfield not found for delete. Id: {}", id);
                    return new FormFieldNotFoundException("Formfield tapilmadi:" + id);
                });
        formFieldRepository.delete(formField);
        log.info("Formfield deleted successfully. Id: {}", id);
    }


}
