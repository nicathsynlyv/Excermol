//package com.example.Excermol.Service;
//
//import com.example.Excermol.entity.dtos.AttachmentResponseDTO;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//public interface AttachmentService {
//
//    // email-ə fayl yüklə
//    AttachmentResponseDTO uploadToEmail(Long emailId, Long userId, MultipartFile file) throws IOException;
//
//    // task-a fayl yüklə
//    AttachmentResponseDTO uploadToTask(Long taskId, Long userId, MultipartFile file) throws IOException;
//
//    // email-ə görə bütün attachmentlər
//    List<AttachmentResponseDTO> getAttachmentsByEmailId(Long emailId);
//
//    // task-a görə bütün attachmentlər
//    List<AttachmentResponseDTO> getAttachmentsByTaskId(Long taskId);
//
//    // id-yə görə attachment
//    AttachmentResponseDTO getAttachmentById(Long id);
//
//    // attachment sil
//    void deleteAttachment(Long id) throws IOException;
//}