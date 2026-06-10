//package com.example.Excermol.repository;
//
//import com.example.Excermol.entity.Attachment;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
//
//    // email-ə görə bütün attachmentlər
//    List<Attachment> findAllByEmailId(Long emailId);
//
//    // task-a görə bütün attachmentlər
//    List<Attachment> findAllByTaskId(Long taskId);
//
//    // user-ə görə bütün attachmentlər
//    List<Attachment> findAllByUploadedById(Long userId);
//}