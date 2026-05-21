package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.UserService;
import com.example.Excermol.entity.User;
import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import com.example.Excermol.exception.EmailAlreadyExistsException;
import com.example.Excermol.exception.UserNotFoundException;
import com.example.Excermol.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("İstifadəçi tapılmadı");
        }

        userRepository.deleteById(id);
    }

    @Override
    public User createUser(User user) {

        log.info("Yeni istifadəçi yaradılır: {}", user.getEmail());

        if (userRepository.existsByEmail(user.getEmail())) {
           throw new EmailAlreadyExistsException("Bu email artiq istifade edilib");
        }

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchUsers(String keyword, Pageable pageable) {

        return userRepository.findByFullNameContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı"));

        // fullname
        existingUser.setFullName(updatedUser.getFullName());

        // email dəyişirsə yoxla
        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {

            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new EmailAlreadyExistsException("Bu email artıq istifadə olunur");
            }

            existingUser.setEmail(updatedUser.getEmail());
        }

        // role & status
        existingUser.setRole(updatedUser.getRole());
        existingUser.setStatus(updatedUser.getStatus());

        return userRepository.save(existingUser);
    }
    @Override
    public void updateLastLogin(String email) {

        userRepository.findByEmail(email).ifPresent(user -> {

            user.setLastLogin(LocalDateTime.now());

            userRepository.save(user);
        });
    }

    @Override
    public List<User> findActiveUsers() {

        return userRepository.findByStatus(UserStatus.ACTIVE);
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
    public List<User> findByStatus(UserStatus status) {

        return userRepository.findByStatus(status);
    }

    @Override
    public List<User> findByRole(UserRole role) {

        return userRepository.findByRole(role);
    }
}
