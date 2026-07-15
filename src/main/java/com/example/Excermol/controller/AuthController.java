package com.example.Excermol.controller;

import com.example.Excermol.entity.dtos.AuthResponseDTO;
import com.example.Excermol.entity.dtos.LoginRequestDTO;
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

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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
}
