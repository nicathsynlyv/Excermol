package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.AttachmentService;
import com.example.Excermol.entity.Attachment;
import com.example.Excermol.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    // BaseService metodları
    @Override
    public List<Attachment> getAll() {
        return attachmentRepository.findAll();
    }

    @Override
    public Optional<Attachment> getById(Long id) {
        return attachmentRepository.findById(id);
    }

    @Override
    public Attachment save(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    @Override
    public void deleteById(Long id) {
        attachmentRepository.deleteById(id);
    }

    // AttachmentService spesifik metodlar
    @Override
    public List<Attachment> findByTask_Id(Long taskId) {
        return attachmentRepository.findByTask_Id(taskId);
    }

    @Override
    public List<Attachment> findByEmail_Id(Long emailId) {
        return attachmentRepository.findByEmail_Id(emailId);
    }
}
