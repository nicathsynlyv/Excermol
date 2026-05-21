package com.example.Excermol.controller;

import com.example.Excermol.Service.UserService;
import com.example.Excermol.entity.User;
import com.example.Excermol.Service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
//@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Constructor Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "Yeni istifadəçi yaratmaq")
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

        User createdUser = userService.createUser(user);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "ID üzrə istifadəçi tapmaq")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {

        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Email üzrə istifadəçi tapmaq")
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {

        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Bütün istifadəçiləri gətirmək")
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {

        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @Operation(summary = "İstifadəçi axtarışı")
    @GetMapping("/search")
    public ResponseEntity<Page<User>> searchUsers(
            @RequestParam String keyword,
            Pageable pageable) {

        return ResponseEntity.ok(userService.searchUsers(keyword, pageable));
    }

    @Operation(summary = "İstifadəçini yenilə")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser) {

        User user = userService.updateUser(id, updatedUser);

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "İstifadəçini sil")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Son login vaxtını yenilə")
    @PutMapping("/last-login")
    public ResponseEntity<Void> updateLastLogin(@RequestParam String email) {

        userService.updateLastLogin(email);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Aktiv istifadəçilər")
    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {

        return ResponseEntity.ok(userService.findActiveUsers());
    }

    @Operation(summary = "Aktiv istifadəçi sayı")
    @GetMapping("/active/count")
    public ResponseEntity<Long> getActiveUserCount() {

        return ResponseEntity.ok(userService.getActiveUserCount());
    }
}
