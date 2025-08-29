//package com.example.Excermol.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "forms")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//
//public class Form {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//
//    private String name; // Form adı
//
//    @Column(name = "public_link")
//    private String publicLink; // Link
//
//    private boolean published; // Post/Publish
//
//    private LocalDateTime lastActivityAt; // Son dəyişmə vaxtı
//    //form response ile
////    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
////    private List<FormResponse> responses = new ArrayList<>();
//
////    @Transient
////    public int getResponsesCount() {
////        return responses != null ? responses.size() : 0;
////    }
//
//    //user ile
//    @ManyToOne
//    @JoinColumn(name = "owner_id")
//    private User owner;
//
//    //form routing ile
////    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
////    private List<FormRouting> routings = new ArrayList<>();
////
//
//}
