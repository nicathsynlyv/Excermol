package com.example.Excermol.controller;

import com.example.Excermol.entity.User;
import com.example.Excermol.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Yeni istifadəçi yaratmaq
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // İstifadəçini ID ilə əldə etmək
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> userOpt = userService.findById(id);
        return userOpt
                .map(user -> ResponseEntity.ok(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // İstifadəçilərin səhifələnmiş siyahısı
    @GetMapping
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.findAll(pageable);
        return ResponseEntity.ok(users);
    }

    // İstifadəçiləri açar sözlə axtarmaq (səhifələnmiş)
    @GetMapping("/search")
    public ResponseEntity<Page<User>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.searchUsers(keyword, pageable);
        return ResponseEntity.ok(users);
    }

    // İstifadəçini yeniləmək
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id,
                                           @Valid @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // İstifadəçini silmək
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Aktiv istifadəçilərin sayı
    @GetMapping("/active/count")
    public ResponseEntity<Long> getActiveUserCount() {
        Long count = userService.getActiveUserCount();
        return ResponseEntity.ok(count);
    }

    // Aktiv istifadəçilərin siyahısı
    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> activeUsers = userService.findActiveUsers();
        return ResponseEntity.ok(activeUsers);
    }
}
