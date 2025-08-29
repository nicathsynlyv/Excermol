//package com.example.Excermol.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "form_routings")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class FormRouting {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // Form ilə əlaqə
//    @ManyToOne
//    @JoinColumn(name = "form_id")
//    private Form form;
//
//    private String workEmailAddress;
//    private String containsValue;
//    private String redirectTo;
//    private String companyEmailName;
//}
