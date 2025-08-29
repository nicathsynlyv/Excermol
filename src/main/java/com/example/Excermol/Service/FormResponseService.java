//package com.example.Excermol.Service;
//
//import com.example.Excermol.entity.Form;
//import com.example.Excermol.entity.FormResponse;
//import com.example.Excermol.repository.FormRepository;
//import com.example.Excermol.repository.FormResponseRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//
//
//public class FormResponseService {
//    private final FormResponseRepository formResponseRepository;
//    private final FormRepository formRepository;
//
//    public List<FormResponse> getResponsesForForm(Long formId) {
//        return formResponseRepository.findByFormId(formId);
//    }
//
//    public FormResponse addResponse(Long formId, String firstName) {
//        Form form = formRepository.findById(formId).orElseThrow();
//        FormResponse response = FormResponse.builder()
//                .firstName(firstName)
//                .submittedAt(LocalDateTime.now())
//                .form(form)
//                .build();
//        return formResponseRepository.save(response);
//    }
//}
