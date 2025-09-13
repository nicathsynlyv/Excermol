package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;   // Marketing, Urgent, Report...
    private String color;  // rəng kodu (#FF0000 və s.)


    // Tag-ə bir neçə şəxs aid ola bilər (Many-to-Many)
    @ManyToMany(mappedBy = "tags")
    private Set<Person> persons = new HashSet<>();
}
