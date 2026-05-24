package com.example.Excermol.mapper;

import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.UserRequestDto;
import com.example.Excermol.entity.dtos.UserResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    // RequestDto -> Entity
    public User toEntity(UserRequestDto dto) {

        User user = new User();

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        user.setStatus(dto.getStatus());
        user.setRole(dto.getRole());

        return user;
    }

    // Entity -> ResponseDto
    public UserResponseDto toResponse(User user) {

        UserResponseDto dto = new UserResponseDto();

        dto.setId(user.getId());

        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());

        dto.setStatus(user.getStatus());
        dto.setRole(user.getRole());

        dto.setLastLogin(user.getLastLogin());

        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }

    // List<Entity> -> List<ResponseDto>
    public List<UserResponseDto> toResponseList(List<User> users) {

        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}