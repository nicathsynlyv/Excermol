package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.FormResponseService;
import com.example.Excermol.entity.*;
import com.example.Excermol.entity.dtos.FormAnswerRequestDTO;
import com.example.Excermol.entity.dtos.FormSubmitRequestDTO;
import com.example.Excermol.entity.dtos.FormSubmitResponseDTO;
import com.example.Excermol.exception.FormFieldNotFoundException;
import com.example.Excermol.exception.FormNotFoundException;
import com.example.Excermol.exception.FormResponseNotFoundException;
import com.example.Excermol.exception.PersonNotFoundException;
import com.example.Excermol.mapper.FormAnswerMapper;
import com.example.Excermol.mapper.FormResponseMapper;
import com.example.Excermol.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class FormResponseServiceImpl implements FormResponseService {

    private final FormResponseRepository formResponseRepository;
    private final FormResponseAnswerRepository formResponseAnswerRepository;
    private final FormRepository formRepository;
    private final FormFieldRepository formFieldRepository;
    private final PersonRepository personRepository;
    private final FormResponseMapper formResponseMapper;
    private final FormAnswerMapper formAnswerMapper;

    public FormResponseServiceImpl(FormResponseRepository formResponseRepository,
                                   FormResponseAnswerRepository formResponseAnswerRepository,
                                   FormRepository formRepository,
                                   FormFieldRepository formFieldRepository,
                                   PersonRepository personRepository,
                                   FormResponseMapper formResponseMapper,
                                   FormAnswerMapper formAnswerMapper) {
        this.formResponseRepository = formResponseRepository;
        this.formResponseAnswerRepository = formResponseAnswerRepository;
        this.formRepository = formRepository;
        this.formFieldRepository = formFieldRepository;
        this.personRepository = personRepository;
        this.formResponseMapper = formResponseMapper;
        this.formAnswerMapper = formAnswerMapper;
    }

    //1
    @Override
    public FormSubmitResponseDTO submitForm(FormSubmitRequestDTO dto) {
        log.info("Submitting form with id: {}", dto.getFormId());
        // 1 - form tap
        Form form = formRepository.findById(dto.getFormId())
                .orElseThrow(() -> {
                    log.warn("Form not found with id: {}", dto.getFormId());
                    return new FormNotFoundException("Form tapilmadi:" + dto.getFormId());
                });

        // 2 - formResponse yarat
        FormResponse formResponse = formResponseMapper.toEntity(dto);
        formResponse.setForm(form);

        // 3 - contact set et (optional)
        if (dto.getContactId() != null) {
            log.info("Setting contact with id: {}", dto.getContactId());
            Person contact = personRepository.findById(dto.getContactId())
                    .orElseThrow(() -> {
                        log.warn("Person not found with id: {}", dto.getContactId());
                        return new PersonNotFoundException("Person tapilmadi:" + dto.getContactId());
                    });
            formResponse.setContact(contact);
        }

        // 4 - formResponse saxla
        FormResponse saved = formResponseRepository.save(formResponse);
        log.info("Form response saved with id: {}", saved.getId());

        // 5 - hər answer-ı yarat və saxla
        if (dto.getAnswers() != null) {
            log.info("Saving {} answers  for response id: {}", dto.getAnswers().size(), saved.getId());
            for (FormAnswerRequestDTO answerDto : dto.getAnswers()) {
                FormField formField = formFieldRepository.findById(answerDto.getFormFieldId())
                        .orElseThrow(() -> {
                            log.warn("Formfield not found with id: {}", answerDto.getFormFieldId());
                            return new FormFieldNotFoundException("Formfield tapilmadi:" + answerDto.getFormFieldId());
                        });
                FormResponseAnswer answer = formAnswerMapper.toEntity(answerDto);
                answer.setFormField(formField);
                answer.setFormResponse(saved);

                formResponseAnswerRepository.save(answer);
            }
        }

        // 6 - form responsesCount artır
        form.setResponsesCount(form.getResponsesCount() + 1);
        formRepository.save(form);
        log.info("Form response count incremented for from id: {}", form.getId());

        // 7 - dolu response qaytar
        FormResponse fullResponse = formResponseRepository.findById(saved.getId())
                .orElseThrow(() -> {
                    log.warn("FormResponse not found with id: {}", saved.getId());
                    return new FormResponseNotFoundException("Form response tapilmadi:" + saved.getId());
                });
        log.info("Form submitted successfully with response id: {}", saved.getId());
        return formResponseMapper.toResponseDTO(fullResponse);
    }

    @Override
    public List<FormSubmitResponseDTO> getResponsesByFormId(Long formId) {
        log.info("Fetching responses for form id: {}", formId);
        List<FormSubmitResponseDTO> responses = formResponseRepository.findAllByFormId(formId)
                .stream()
                .map(formResponseMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} responses for form id: {}", responses.size(), formId);
        return responses;
    }

    @Override
    public FormSubmitResponseDTO getResponseById(Long id) {
        log.info("Fetching responses with id: {}", id);
        FormResponse formResponse = formResponseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("FormResponse not found with id: {}", id);
                    return new FormResponseNotFoundException("FormResponse tapilmadi:" + id);
                });
        log.info("Form response found with id: {} ", id);
        return formResponseMapper.toResponseDTO(formResponse);
    }

    @Override
    public void deleteResponse(Long id) {
        log.info("Deleting form response with id: {}", id);
        FormResponse formResponse = formResponseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("FormResponse not found for deletion. Id: {}", id);
                    return new FormResponseNotFoundException("FormResponse tapilmadi:" + id);
                });
        // responsesCount azalt
        Form form = formResponse.getForm();
        if (form.getResponsesCount() > 0) {
            form.setResponsesCount(form.getResponsesCount() - 1);
            formRepository.save(form);
            log.info("Form responses count decremented for form id: {}", form.getId());
        }

        formResponseRepository.delete(formResponse);
        log.info("Form response deleted Successfully. Id: {} ", id);
    }
}
