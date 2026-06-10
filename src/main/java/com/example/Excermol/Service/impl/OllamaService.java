//package com.example.Excermol.Service.impl;
//
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class OllamaService {
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public String askAI(String prompt) {
//
//        String url = "http://localhost:11434/api/generate";
//
//        String body = """
//        {
//          "model":"llama3",
//          "prompt":"%s",
//          "stream":false
//        }
//        """.formatted(prompt);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<String> entity =
//                new HttpEntity<>(body, headers);
//
//        ResponseEntity<String> response =
//                restTemplate.postForEntity(
//                        url,
//                        entity,
//                        String.class
//                );
//
//        return response.getBody();
//    }
//}
