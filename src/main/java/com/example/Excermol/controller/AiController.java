//package com.example.Excermol.controller;
//
//import com.example.Excermol.Service.impl.OllamaService;
//import com.example.Excermol.entity.dtos.ChatRequest;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/ai")
//public class AiController {
//
//    private final OllamaService ollamaService;
//
//    public AiController(OllamaService ollamaService) {
//        this.ollamaService = ollamaService;
//    }
//
//    @PostMapping("/chat")
//    public String chat(@RequestBody ChatRequest request) {
//        return ollamaService.askAI(request.getMessage());
//    }
//}
