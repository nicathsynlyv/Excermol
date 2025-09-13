package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.ResponseService;
import com.example.Excermol.entity.Response;
import com.example.Excermol.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponseServiceImpl implements ResponseService {
    private final ResponseRepository responseRepository;

    @Autowired
    public ResponseServiceImpl(ResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    @Override
    public List<Response> getAll() {
        return responseRepository.findAll();
    }

    @Override
    public Optional<Response> getById(Long id) {
        return responseRepository.findById(id);
    }

    @Override
    public Response save(Response entity) {
        return responseRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        responseRepository.deleteById(id);
    }

    public long countByFormId(Long formId) {
        return responseRepository.countByFormId(formId);
    }
}
