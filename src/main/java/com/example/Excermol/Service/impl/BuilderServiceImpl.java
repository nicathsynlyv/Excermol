package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.BuilderService;
import com.example.Excermol.entity.Builder;
import com.example.Excermol.repository.BuilderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuilderServiceImpl implements BuilderService {
    private final BuilderRepository builderRepository;

    @Autowired
    public BuilderServiceImpl(BuilderRepository builderRepository) {
        this.builderRepository = builderRepository;
    }

    @Override
    public List<Builder> getAll() {
        return builderRepository.findAll();
    }

    @Override
    public Optional<Builder> getById(Long id) {
        return builderRepository.findById(id);
    }

    @Override
    public Builder save(Builder entity) {
        return builderRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        builderRepository.deleteById(id);
    }

    public Optional<Builder> findByFormId(Long formId) {
        return builderRepository.findByFormId(formId);
    }

    public Optional<Builder> findByFormTitle(String title) {
        return builderRepository.findByFormTitle(title);
    }


}
