//package com.example.Excermol.Service;
//
//import com.example.Excermol.entity.Form;
//import com.example.Excermol.repository.FormRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//
//public class FormService {
//    private final FormRepository formRepository;
//
//    public List<Form> getAllForms() {
//        return formRepository.findAll();
//    }
//
//    public Form createForm(Form form) {
//        form.setLastActivityAt(LocalDateTime.now());
//        return formRepository.save(form);
//    }
//
//    public void deleteForm(Long id) {
//        formRepository.deleteById(id);
//    }
//
//    public Form updateFormPublish(Long id, boolean published) {
//        Form form = formRepository.findById(id).orElseThrow();
//        form.setPublished(published);
//        form.setLastActivityAt(LocalDateTime.now());
//        return formRepository.save(form);
//    }
//}
