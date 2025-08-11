package com.example.Excermol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "persons", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}) // Email unikal olsun
})
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Status cannot be blank")
    @Column(nullable = false, length = 50)
    private String status;

    @PastOrPresent(message = "Last interaction date cannot be in the future")
    @Column(name = "last_interaction")
    private LocalDateTime lastInteraction;

    @DecimalMin(value = "0.0", inclusive = true, message = "Lead value must be positive")
    @Digits(integer = 10, fraction = 2, message = "Lead value must have up to 10 digits and 2 decimal places")
    @Column(name = "lead_value", precision = 12, scale = 2)
    private BigDecimal leadValue;

    @jakarta.validation.constraints.Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    @Column(nullable = false, unique = true, length = 150)
    private String email;


    @ElementCollection
    @CollectionTable(
            name = "person_tags",
            joinColumns = @JoinColumn(name = "person_id")
    )
    @Column(name = "tag", length = 50)
    private Set<String> tags;



    @Pattern(regexp = "\\+?[0-9]{7,15}", message = "Phone number must be valid")
    @Column(length = 20)
    private String phone;

    @Size(max = 255)
    @Column(length = 255)
    private String website;

    @Size(max = 255)
    @Column(name = "message_link", length = 255)
    private String messageLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Email> emails;
}