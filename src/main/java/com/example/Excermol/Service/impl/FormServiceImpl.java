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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User tapılmadı: " + ownerId));

        Form form = formMapper.toEntity(dto);
        form.setOwner(owner);

        Form saved = formRepository.save(form);
        return formMapper.toResponseDTO(saved);
    }

    @Override
    public List<FormResponseDTO> getAllForms() {
        return formRepository.findAll()
                .stream()
                .map(formMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FormResponseDTO getFormById(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new FormNotFoundException("Form tapılmadı: " + id));
        return formMapper.toResponseDTO(form);
    }

    @Override
    public List<FormResponseDTO> getFormsByOwner(Long ownerId) {
        return formRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(formMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FormResponseDTO updateForm(Long id, FormUpdateRequestDTO dto) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new FormNotFoundException("Form tapılmadı: " + id));

        formMapper.updateEntity(form, dto);

        Form updated = formRepository.save(form);
        return formMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteForm(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new FormNotFoundException("Form tapılmadı: " + id));
        formRepository.delete(form);
    }

    @Override
    public FormResponseDTO toggleFormStatus(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new FormNotFoundException("Form tapılmadı: " + id));

        if (form.getStatus() == FormStatus.PUBLISHED) {
            form.setStatus(FormStatus.DRAFT);
        } else {
            form.setStatus(FormStatus.PUBLISHED);
        }

        Form updated = formRepository.save(form);
        return formMapper.toResponseDTO(updated);
    }
}
