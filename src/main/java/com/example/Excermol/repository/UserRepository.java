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
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByStatus(UserStatus status);

    List<User> findByRole(UserRole role);

    @Query("SELECT u FROM User u WHERE u.firstName ILIKE %:keyword% OR u.lastName ILIKE %:keyword% OR u.email ILIKE %:keyword%")
    Page<User> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    List<User> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
    Long countByStatus(@Param("status") UserStatus status);

}
