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

import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;

@Service
@Transactional
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

    @Override
    public FormFieldResponseDTO createField(FormFieldCreateRequestDTO dto) {
        Form form = formRepository.findById(dto.getFormId())
                .orElseThrow(() -> new FormNotFoundException("Form tapılmadı: " + dto.getFormId()));

        FormField formField = formFieldMapper.toEntity(dto);
        formField.setForm(form);

        FormField saved = formFieldRepository.save(formField);
        return formFieldMapper.toResponseDTO(saved);
    }

    @Override
    public List<FormFieldResponseDTO> getFieldsByFormId(Long formId) {
        return formFieldRepository.findAllByFormIdOrderByFieldOrderAsc(formId)
                .stream()
                .map(formFieldMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FormFieldResponseDTO getFieldById(Long id) {
        FormField formField = formFieldRepository.findById(id)
                .orElseThrow(() -> new FormFieldNotFoundException("FormField tapılmadı: " + id));
        return formFieldMapper.toResponseDTO(formField);
    }

    @Override
    public FormFieldResponseDTO updateField(Long id, FormFieldUpdateRequestDTO dto) {
        FormField formField = formFieldRepository.findById(id)
                .orElseThrow(() -> new FormFieldNotFoundException("FormField tapılmadı: " + id));

        formFieldMapper.updateEntity(formField, dto);

        FormField updated = formFieldRepository.save(formField);
        return formFieldMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteField(Long id) {
        FormField formField = formFieldRepository.findById(id)
                .orElseThrow(() -> new FormFieldNotFoundException("FormField tapılmadı: " + id));
        formFieldRepository.delete(formField);
    }


}
