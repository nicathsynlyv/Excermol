package com.example.Excermol.Service;

import com.example.Excermol.entity.User;
import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService extends BaseService<User,Long>{
    User createUser(User user);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Page<User> searchUsers(String keyword, Pageable pageable);

    User updateUser(Long id, User updatedUser);

    void updateLastLogin(String email);

    List<User> findActiveUsers();

    Long getActiveUserCount();

    boolean existsByEmail(String email);

    List<User> findByStatus(UserStatus status);

    List<User> findByRole(UserRole role);

}
