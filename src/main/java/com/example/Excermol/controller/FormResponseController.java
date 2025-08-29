//package com.example.Excermol.controller;
//
//import com.example.Excermol.Service.FormResponseService;
//import com.example.Excermol.entity.FormResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/forms/{formId}/responses")
//@RequiredArgsConstructor
//public class FormResponseController {
//    private final FormResponseService formResponseService;
//
//    @GetMapping
//    public List<FormResponse> getResponses(@PathVariable Long formId) {
//        return formResponseService.getResponsesForForm(formId);
//    }
//
//    @PostMapping
//    public FormResponse addResponse(@PathVariable Long formId, @RequestParam String firstName) {
//        return formResponseService.addResponse(formId, firstName);
//    }
//}
