package com.example.Excermol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2,max = 50)
    @Column(nullable = false, length = 50)
    private String name;   // Marketing, Urgent, Report...


    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    @Column(length = 7)
    private String color;  // rəng kodu (#FF0000 və s.)


    // Tag-ə bir neçə şəxs aid ola bilər (Many-to-Many)
    @ManyToMany(mappedBy = "tags")
    private Set<Person> persons = new HashSet<>();

    // taga bir nece task aid ola biler
    @ManyToMany(mappedBy = "tags")
    private Set<Task> tasks = new HashSet<>();
}
