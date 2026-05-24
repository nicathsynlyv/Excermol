package com.example.Excermol.controller;

import com.example.Excermol.Service.UserService;
import com.example.Excermol.entity.dtos.UserRequestDto;
import com.example.Excermol.entity.dtos.UserResponseDto;
import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Yeni istifadəçi yaratmaq")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "İstifadəçi uğurla yaradıldı"),
            @ApiResponse(responseCode = "400", description = "Validasiya xətası"),
            @ApiResponse(responseCode = "409", description = "Bu email artıq mövcuddur")
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody UserRequestDto dto) {

        UserResponseDto createdUser =
                userService.createUser(dto);

        return new ResponseEntity<>(
                createdUser,
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Bütün istifadəçilər")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Siyahı uğurla qaytarıldı")
    })
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAll()
        );
    }

    @Operation(summary = "ID üzrə istifadəçi tapmaq")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "İstifadəçi tapıldı"),
            @ApiResponse(responseCode = "404", description = "İstifadəçi tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getById(id)
        );
    }

    @Operation(summary = "İstifadəçi axtarışı")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Axtarış nəticələri qaytarıldı"),
            @ApiResponse(responseCode = "400", description = "Keyword boş ola bilməz")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDto>> searchUsers(
            @RequestParam String keyword,
            Pageable pageable) {

        return ResponseEntity.ok(
                userService.searchUsers(keyword, pageable)
        );
    }

    @Operation(summary = "İstifadəçini yenilə")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "İstifadəçi uğurla yeniləndi"),
            @ApiResponse(responseCode = "400", description = "Validasiya xətası"),
            @ApiResponse(responseCode = "404", description = "İstifadəçi tapılmadı"),
            @ApiResponse(responseCode = "409", description = "Bu email artıq mövcuddur")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto dto) {

        UserResponseDto updatedUser =
                userService.updateUser(id, dto);

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "İstifadəçini sil")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "İstifadəçi uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "İstifadəçi tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Son login vaxtını yenilə")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Son login vaxtı yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Bu email ilə istifadəçi tapılmadı")
    })
    @PatchMapping("/last-login")
    public ResponseEntity<Void> updateLastLogin(
            @RequestParam String email) {

        userService.updateLastLogin(email);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Aktiv istifadəçilər")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aktiv istifadəçilər qaytarıldı")
    })
    @GetMapping("/active")
    public ResponseEntity<List<UserResponseDto>> getActiveUsers() {

        return ResponseEntity.ok(
                userService.findActiveUsers()
        );
    }

    @Operation(summary = "Aktiv istifadəçi sayı")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aktiv istifadəçi sayı qaytarıldı")
    })
    @GetMapping("/active/count")
    public ResponseEntity<Long> getActiveUserCount() {

        return ResponseEntity.ok(
                userService.getActiveUserCount()
        );
    }

    @Operation(summary = "Statusa görə istifadəçilər")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "İstifadəçilər uğurla qaytarıldı"),
            @ApiResponse(responseCode = "400", description = "Yanlış status dəyəri")
    })
    @GetMapping("/by-status")
    public ResponseEntity<List<UserResponseDto>> getUsersByStatus(
            @RequestParam UserStatus status) {

        return ResponseEntity.ok(
                userService.findByStatus(status)
        );
    }

    @Operation(summary = "Rola görə istifadəçilər")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "İstifadəçilər uğurla qaytarıldı"),
            @ApiResponse(responseCode = "400", description = "Yanlış rol dəyəri")
    })
    @GetMapping("/by-role")
    public ResponseEntity<List<UserResponseDto>> getUsersByRole(
            @RequestParam UserRole role) {

        return ResponseEntity.ok(
                userService.findByRole(role)
        );
    }
}