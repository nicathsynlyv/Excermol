package com.example.Excermol.entity;

import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Ad boş ola bilmez")
    @Size(min = 2, max = 50, message = "Ad 2-50 simvol arasında olmalıdır")
    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;


    @Email(message = "Email formatı düzgün deyil")
    @NotBlank(message = "Email boş ola bilməz")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Şifrə boş ola bilməz")
    @Size(min = 6, message = "Şifrə minimum 6 simvol olmalıdır")
    @Column(nullable = false)
    private String password;




    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;


    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Utility methods

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

//task entitysi ile elaqe
    @ManyToMany(mappedBy = "assignees")
    private Set<Task> tasks;
//comments entitysi ile elaqe
    @OneToMany(mappedBy = "author")
    private Set<Comment> comments;
//attachments entitysi ile elaqe
    @OneToMany(mappedBy = "uploadedBy")
    private Set<Attachment> attachments;

//pipeline entitysi ile
    @ManyToMany(mappedBy = "assignees")
    private List<Pipeline> pipelines;




//compagins ile elaqe
//    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
//    private List<Campaign> campaigns;
////company ile
//    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Company> companies;
////form ile
//    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Form> forms = new ArrayList<>();


}
