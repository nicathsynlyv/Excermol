package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.FormService;
import com.example.Excermol.entity.Builder;
import com.example.Excermol.entity.Form;
import com.example.Excermol.entity.Response;
import com.example.Excermol.entity.Routing;
import com.example.Excermol.repository.BuilderRepository;
import com.example.Excermol.repository.FormRepository;
import com.example.Excermol.repository.ResponseRepository;
import com.example.Excermol.repository.RoutingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FormServiceImpl implements FormService {

    private final FormRepository formRepository;
    private final BuilderRepository builderRepository;
    private final ResponseRepository responseRepository;
    private final RoutingRepository routingRepository;

    @Autowired
    public FormServiceImpl(FormRepository formRepository,
                           BuilderRepository builderRepository,
                           ResponseRepository responseRepository,
                           RoutingRepository routingRepository) {
        this.formRepository = formRepository;
        this.builderRepository = builderRepository;
        this.responseRepository = responseRepository;
        this.routingRepository = routingRepository;
    }

    @Override
    public List<Form> getAll() {
        return formRepository.findAllWithResponses();
    }

    @Override
    public Optional<Form> getById(Long id) {
        return formRepository.findById(id);
    }

    @Override
    public Form save(Form form) {
        // Form-u saxla
        Form savedForm = formRepository.save(form);

        // Əgər builder varsa saxla
        Builder builder = form.getBuilder();
        if (builder != null) {
            builder.setForm(savedForm);
            builderRepository.save(builder);
        }

        // Əgər routings varsa saxla
        List<Routing> routings = form.getRoutings();
        if (routings != null) {
            for (Routing routing : routings) {
                routing.setForm(savedForm);
                routingRepository.save(routing);
            }
        }

        // Responses-ləri saxla
        List<Response> responses = form.getResponses();
        if (responses != null) {
            for (Response response : responses) {
                response.setForm(savedForm);
                responseRepository.save(response);
            }
        }

        return savedForm;
    }

    @Override
    public void deleteById(Long id) {
        formRepository.deleteById(id);
    }

    // əlavə metodlar
    public List<Form> findByName(String name) {
        return formRepository.findByFormsNameContainingIgnoreCase(name);
    }

    public long countResponses(Long formId) {
        return responseRepository.countByFormId(formId);
    }
}
