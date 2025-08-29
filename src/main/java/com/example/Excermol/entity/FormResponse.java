//package com.example.Excermol.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "form_responses")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class FormResponse {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String firstName; // Cavab sahəsi (şəkildə "First Name" var idi)
//
//    private String lastName;
//
//
//    private LocalDateTime submittedAt; // Göndərilmə vaxtı
//
//    // Burda əlaqəni qururuq email ile
//    @ManyToOne
//    @JoinColumn(name = "email_id")
//    private Email email;
//
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "form_id")
//    private Form form;
//}
