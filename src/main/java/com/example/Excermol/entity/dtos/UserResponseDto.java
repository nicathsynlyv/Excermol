package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDto {

    private Long id;

    private String fullName;

    private String email;

    private UserStatus status;

    private UserRole role;

    private LocalDateTime lastLogin;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}