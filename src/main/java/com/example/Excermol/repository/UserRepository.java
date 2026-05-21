package com.example.Excermol.repository;

import com.example.Excermol.entity.User;
import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByStatus(UserStatus status);
    Long countByStatus(UserStatus status);

    List<User> findByRole(UserRole role);

    Page<User> findByFullNameContainingIgnoreCase(String keyword, Pageable pageable);
}
