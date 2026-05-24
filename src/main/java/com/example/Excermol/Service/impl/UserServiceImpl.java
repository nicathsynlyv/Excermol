package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.UserService;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.UserRequestDto;
import com.example.Excermol.entity.dtos.UserResponseDto;
import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import com.example.Excermol.exception.EmailAlreadyExistsException;
import com.example.Excermol.exception.UserNotFoundException;
import com.example.Excermol.mapper.UserMapper;
import com.example.Excermol.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserResponseDto> getAll(){
        return userMapper.toResponseList(
                userRepository.findAll()
        );
    }


    @Override
    public UserResponseDto getById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("İstifadəçi tapılmadı"));

        return userMapper.toResponse(user);
    }



    @Override
    public void deleteById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User tapilmadi"));
        userRepository.delete(user);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {

        log.info("Yeni istifadəçi yaradılır: {}", dto.getEmail());

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Bu email artiq istifade edilib");
        }

        User user = userMapper.toEntity(dto);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }



    @Override
    public Page<UserResponseDto> searchUsers(
            String keyword,
            Pageable pageable) {

        return userRepository
                .findByFullNameContainingIgnoreCase(keyword, pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("İstifadəçi tapılmadı"));

        existingUser.setFullName(dto.getFullName());

        if (!existingUser.getEmail().equals(dto.getEmail())) {

            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException(
                        "Bu email artıq istifadə olunur");
            }

            existingUser.setEmail(dto.getEmail());
        }

        existingUser.setRole(dto.getRole());
        existingUser.setStatus(dto.getStatus());

        if (dto.getPassword() != null &&
                !dto.getPassword().isBlank()) {

            existingUser.setPassword(dto.getPassword());
        }

        User updated = userRepository.save(existingUser);

        return userMapper.toResponse(updated);
    }
    @Override
    public void updateLastLogin(String email) {

        userRepository.findByEmail(email).ifPresent(user -> {

            user.setLastLogin(LocalDateTime.now());

            userRepository.save(user);
        });
    }

    @Override
    public List<UserResponseDto> findActiveUsers() {

        return userMapper.toResponseList(
                userRepository.findByStatus(UserStatus.ACTIVE)
        );
    }

    @Override
    public Long getActiveUserCount() {

        return userRepository.countByStatus(UserStatus.ACTIVE);
    }

    @Override
    public boolean existsByEmail(String email) {

        return userRepository.existsByEmail(email);
    }

    @Override
    public List<UserResponseDto> findByStatus(UserStatus status) {

        return userMapper.toResponseList(
                userRepository.findByStatus(status)
        );
    }

    @Override
    public List<UserResponseDto> findByRole(UserRole role) {

        return userMapper.toResponseList(
                userRepository.findByRole(role)
        );
    }
}
