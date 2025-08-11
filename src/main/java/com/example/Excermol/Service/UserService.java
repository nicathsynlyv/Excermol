package com.example.Excermol.Service;

import com.example.Excermol.entity.User;
import com.example.Excermol.enums.UserStatus;
import com.example.Excermol.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        log.info("Yeni istifadəçi yaradılır: {}", user.getEmail());

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Bu email artıq istifadə edilib: " + user.getEmail());
        }

        // Şifrəni encode et
//        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public Page<User> searchUsers(String keyword, Pageable pageable) {
        return userRepository.findByKeyword(keyword, pageable);
    }

    public User updateUser(Integer id, User updatedUser) {
        log.info("İstifadəçi yenilənir: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı: " + id));

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setProfileImageUrl(updatedUser.getProfileImageUrl());
        existingUser.setStatus(updatedUser.getStatus());
        existingUser.setRole(updatedUser.getRole());

        // Email dəyişdirilirsə, unique olduğunu yoxla
        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new RuntimeException("Bu email artıq istifadə edilib: " + updatedUser.getEmail());
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(Integer id) {
        log.info("İstifadəçi silinir: {}", id);

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("İstifadəçi tapılmadı: " + id);
        }

        userRepository.deleteById(id);
    }

    public void updateLastLogin(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        userOpt.ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    @Transactional
    public List<User> findActiveUsers() {
        return userRepository.findByStatus(UserStatus.ACTIVE);
    }

    @Transactional
    public Long getActiveUserCount() {
        return userRepository.countByStatus(UserStatus.ACTIVE);
    }
}
