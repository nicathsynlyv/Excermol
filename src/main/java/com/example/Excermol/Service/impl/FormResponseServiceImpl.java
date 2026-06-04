package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.FormResponseService;
import com.example.Excermol.entity.*;
import com.example.Excermol.entity.dtos.FormAnswerRequestDTO;
import com.example.Excermol.entity.dtos.FormSubmitRequestDTO;
import com.example.Excermol.entity.dtos.FormSubmitResponseDTO;
import com.example.Excermol.exception.FormNotFoundException;
import com.example.Excermol.exception.FormResponseNotFoundException;
import com.example.Excermol.mapper.FormAnswerMapper;
import com.example.Excermol.mapper.FormResponseMapper;
import com.example.Excermol.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
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

    @Override
    public FormSubmitResponseDTO submitForm(FormSubmitRequestDTO dto) {
        // 1 - form tap
        Form form = formRepository.findById(dto.getFormId())
                .orElseThrow(() -> new FormNotFoundException("Form tapılmadı: " + dto.getFormId()));

        // 2 - formResponse yarat
        FormResponse formResponse = formResponseMapper.toEntity(dto);
        formResponse.setForm(form);

        // 3 - contact set et (optional)
        if (dto.getContactId() != null) {
            Person contact = personRepository.findById(dto.getContactId())
                    .orElseThrow(() -> new RuntimeException("Person tapılmadı: " + dto.getContactId()));
            formResponse.setContact(contact);
        }

        // 4 - formResponse saxla
        FormResponse saved = formResponseRepository.save(formResponse);

        // 5 - hər answer-ı yarat və saxla
        if (dto.getAnswers() != null) {
            for (FormAnswerRequestDTO answerDto : dto.getAnswers()) {
                FormField formField = formFieldRepository.findById(answerDto.getFormFieldId())
                        .orElseThrow(() -> new RuntimeException("FormField tapılmadı: " + answerDto.getFormFieldId()));

                FormResponseAnswer answer = formAnswerMapper.toEntity(answerDto);
                answer.setFormField(formField);
                answer.setFormResponse(saved);

                formResponseAnswerRepository.save(answer);
            }
        }

        // 6 - form responsesCount artır
        form.setResponsesCount(form.getResponsesCount() + 1);
        formRepository.save(form);

        // 7 - dolu response qaytar
        FormResponse fullResponse = formResponseRepository.findById(saved.getId())
                .orElseThrow(() -> new FormResponseNotFoundException("FormResponse tapılmadı: " + saved.getId()));

        return formResponseMapper.toResponseDTO(fullResponse);
    }

    @Override
    public List<FormSubmitResponseDTO> getResponsesByFormId(Long formId) {
        return formResponseRepository.findAllByFormId(formId)
                .stream()
                .map(formResponseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FormSubmitResponseDTO getResponseById(Long id) {
        FormResponse formResponse = formResponseRepository.findById(id)
                .orElseThrow(() -> new FormResponseNotFoundException("FormResponse tapılmadı: " + id));
        return formResponseMapper.toResponseDTO(formResponse);
    }

    @Override
    public void deleteResponse(Long id) {
        FormResponse formResponse = formResponseRepository.findById(id)
                .orElseThrow(() -> new FormResponseNotFoundException("FormResponse tapılmadı: " + id));

        // responsesCount azalt
        Form form = formResponse.getForm();
        if (form.getResponsesCount() > 0) {
            form.setResponsesCount(form.getResponsesCount() - 1);
            formRepository.save(form);
        }

        formResponseRepository.delete(formResponse);
    }
}
