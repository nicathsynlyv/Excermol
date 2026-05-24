package com.example.Excermol.Service;


import com.example.Excermol.entity.dtos.UserRequestDto;
import com.example.Excermol.entity.dtos.UserResponseDto;
import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;


public interface UserService {

    List<UserResponseDto> getAll();

    UserResponseDto getById(Long id);

    void deleteById(Long id);

    UserResponseDto createUser(UserRequestDto dto);


    Page<UserResponseDto> searchUsers(String keyword, Pageable pageable);

    UserResponseDto updateUser(Long id, UserRequestDto dto);

    void updateLastLogin(String email);

    List<UserResponseDto> findActiveUsers();

    Long getActiveUserCount();

    boolean existsByEmail(String email);

    List<UserResponseDto> findByStatus(UserStatus status);

    List<UserResponseDto> findByRole(UserRole role);

}
