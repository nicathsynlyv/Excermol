//package com.example.Excermol.Service.impl;
//
//import com.example.Excermol.Service.AttachmentService;
//import com.example.Excermol.entity.Attachment;
//import com.example.Excermol.entity.Email;
//import com.example.Excermol.entity.Task;
//import com.example.Excermol.entity.User;
//import com.example.Excermol.entity.dtos.AttachmentResponseDTO;
//import com.example.Excermol.exception.AttachmentNotFoundException;
//import com.example.Excermol.exception.EmailNotFoundException;
//import com.example.Excermol.exception.TaskNotFoundException;
//import com.example.Excermol.exception.UserNotFoundException;
//import com.example.Excermol.mapper.AttachmentMapper;
//import com.example.Excermol.repository.AttachmentRepository;
//import com.example.Excermol.repository.EmailRepository;
//import com.example.Excermol.repository.TaskRepository;
//import com.example.Excermol.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//public class AttachmentServiceImpl implements AttachmentService {
//
//    private final AttachmentRepository attachmentRepository;
//    private final EmailRepository emailRepository;
//    private final TaskRepository taskRepository;
//    private final UserRepository userRepository;
//    private final AttachmentMapper attachmentMapper;
//
//    @Value("${file.upload-dir:uploads/}")
//    private String uploadDir;
//
//    public AttachmentServiceImpl(AttachmentRepository attachmentRepository,
//                                 EmailRepository emailRepository,
//                                 TaskRepository taskRepository,
//                                 UserRepository userRepository,
//                                 AttachmentMapper attachmentMapper) {
//        this.attachmentRepository = attachmentRepository;
//        this.emailRepository = emailRepository;
//        this.taskRepository = taskRepository;
//        this.userRepository = userRepository;
//        this.attachmentMapper = attachmentMapper;
//    }
//
//    @Override
//    public AttachmentResponseDTO uploadToEmail(Long emailId, Long userId,
//                                               MultipartFile file) throws IOException {
//        // email tap
//        Email email = emailRepository.findById(emailId)
//                .orElseThrow(() -> new EmailNotFoundException("Email tapılmadı: " + emailId));
//
//        // user tap
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException("User tapılmadı: " + userId));
//
//        // faylı saxla
//        String filePath = saveFile(file);
//
//        // attachment yarat
//        Attachment attachment = Attachment.builder()
//                .fileName(file.getOriginalFilename())
//                .fileUrl(filePath)
//                .fileType(file.getContentType())
//                .email(email)
//                .uploadedBy(user)
//                .build();
//
//        Attachment saved = attachmentRepository.save(attachment);
//        return attachmentMapper.toResponseDTO(saved);
//    }
//
//    @Override
//    public AttachmentResponseDTO uploadToTask(Long taskId, Long userId,
//                                              MultipartFile file) throws IOException {
//        // task tap
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new TaskNotFoundException("Task tapılmadı: " + taskId));
//
//        // user tap
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException("User tapılmadı: " + userId));
//
//        // faylı saxla
//        String filePath = saveFile(file);
//
//        // attachment yarat
//        Attachment attachment = Attachment.builder()
//                .fileName(file.getOriginalFilename())
//                .fileUrl(filePath)
//                .fileType(file.getContentType())
//                .task(task)
//                .uploadedBy(user)
//                .build();
//
//        Attachment saved = attachmentRepository.save(attachment);
//        return attachmentMapper.toResponseDTO(saved);
//    }
//
//    @Override
//    public List<AttachmentResponseDTO> getAttachmentsByEmailId(Long emailId) {
//        return attachmentRepository.findAllByEmailId(emailId)
//                .stream()
//                .map(attachmentMapper::toResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<AttachmentResponseDTO> getAttachmentsByTaskId(Long taskId) {
//        return attachmentRepository.findAllByTaskId(taskId)
//                .stream()
//                .map(attachmentMapper::toResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public AttachmentResponseDTO getAttachmentById(Long id) {
//        Attachment attachment = attachmentRepository.findById(id)
//                .orElseThrow(() -> new AttachmentNotFoundException("Attachment tapılmadı: " + id));
//        return attachmentMapper.toResponseDTO(attachment);
//    }
//
//    @Override
//    public void deleteAttachment(Long id) throws IOException {
//        Attachment attachment = attachmentRepository.findById(id)
//                .orElseThrow(() -> new AttachmentNotFoundException("Attachment tapılmadı: " + id));
//
//        // faylı disk-dən sil
//        Path filePath = Paths.get(attachment.getFileUrl());
//        if (Files.exists(filePath)) {
//            Files.delete(filePath);
//        }
//
//        attachmentRepository.delete(attachment);
//    }
//
//    // =========================
//    // FAYL SAXLAMA HELPER
//    // =========================
//    private String saveFile(MultipartFile file) throws IOException {
//        // qovluq yoxdursa yarat
//        Files.createDirectories(Paths.get(uploadDir));
//
//        String filePath = uploadDir + file.getOriginalFilename();
//        Path path = Paths.get(filePath);
//        file.transferTo(path);
//
//        return filePath;
//    }
//}