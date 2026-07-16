package com.example.Excermol.controller;

import com.example.Excermol.Service.UserService;
import com.example.Excermol.Service.impl.UserServiceImpl;
import com.example.Excermol.entity.dtos.*;
import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import com.example.Excermol.security.jwt.JwtUtil;
import com.example.Excermol.security.userdetails.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller",description ="Login əməliyyatları" )
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Operation(summary = "Login - email və şifrə ilə token almaq")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtUtil.generateToken(principal);

        AuthResponseDTO response = new AuthResponseDTO(
                token,
                principal.getUsername(),
                principal.getUser().getRole().name()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Qeydiyyat - yeni istifadəçi yaratmaq")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {

        // RegisterRequestDTO-nu UserRequestDto-ya çeviririk, rolu məcburi USER edirik
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setFullName(dto.getFullName());
        userRequestDto.setEmail(dto.getEmail());
        userRequestDto.setPassword(dto.getPassword());
        userRequestDto.setRole(UserRole.USER);       // məcburi - istifadəçi seçə bilməz
        userRequestDto.setStatus(UserStatus.ACTIVE); // məcburi

        UserResponseDto createdUser = userService.createUser(userRequestDto);

        // Qeydiyyatdan sonra avtomatik login et, token qaytar
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtUtil.generateToken(principal);

        AuthResponseDTO response = new AuthResponseDTO(
                token, principal.getUsername(), principal.getUser().getRole().name()
        );

        return ResponseEntity.status(201).body(response);
    }
}
