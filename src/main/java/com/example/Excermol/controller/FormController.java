//package com.example.Excermol.controller;
//
//import com.example.Excermol.Service.FormService;
//import com.example.Excermol.entity.Form;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/forms")
//@RequiredArgsConstructor
//public class FormController {
//    private final FormService formService;
//
//    @GetMapping
//    public List<Form> getAllForms() {
//        return formService.getAllForms();
//    }
//
//    @PostMapping
//    public Form createForm(@RequestBody Form form) {
//        return formService.createForm(form);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteForm(@PathVariable Long id) {
//        formService.deleteForm(id);
//    }
//
//    @PatchMapping("/{id}/publish")
//    public Form updatePublish(@PathVariable Long id, @RequestParam boolean published) {
//        return formService.updateFormPublish(id, published);
//    }
//
//}
