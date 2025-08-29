//package com.example.Excermol.controller;
//
//import com.example.Excermol.Service.FormRoutingService;
//import com.example.Excermol.entity.FormRouting;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/form-routings")
//public class FormRoutingController {
//    private final FormRoutingService service;
//
//    public FormRoutingController(FormRoutingService service) {
//        this.service = service;
//    }
//
//    @PostMapping
//    public ResponseEntity<FormRouting> create(@RequestBody FormRouting routing) {
//        return ResponseEntity.ok(service.create(routing));
//    }
//
//    @GetMapping("/form/{formId}")
//    public ResponseEntity<List<FormRouting>> getByForm(@PathVariable Long formId) {
//        return ResponseEntity.ok(service.getByForm(formId));
//    }
//}
