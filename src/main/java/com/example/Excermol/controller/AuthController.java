package com.example.Excermol.controller;

import com.example.Excermol.Service.UserService;
import com.example.Excermol.entity.RefreshToken;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.*;
import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import com.example.Excermol.repository.UserRepository;
import com.example.Excermol.security.jwt.JwtUtil;
import com.example.Excermol.security.jwt.RefreshTokenException;
import com.example.Excermol.security.jwt.RefreshTokenService;
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
@Tag(name = "Auth Controller", description = "Login əməliyyatları")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserService userService,
                          UserRepository userRepository,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Operation(summary = "Login - email və şifrə ilə token almaq")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtUtil.generateToken(principal);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(principal.getUser());

        AuthResponseDTO response = new AuthResponseDTO(
                accessToken,
                refreshToken.getToken(),
                principal.getUsername(),
                principal.getUser().getRole().name()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Qeydiyyat - yeni istifadəçi yaratmaq")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setFullName(dto.getFullName());
        userRequestDto.setEmail(dto.getEmail());
        userRequestDto.setPassword(dto.getPassword());
        userRequestDto.setRole(UserRole.USER);
        userRequestDto.setStatus(UserStatus.ACTIVE);

        userService.createUser(userRequestDto);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtUtil.generateToken(principal);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(principal.getUser());

        AuthResponseDTO response = new AuthResponseDTO(
                accessToken,
                refreshToken.getToken(),
                principal.getUsername(),
                principal.getUser().getRole().name()
        );

        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Access tokeni yeniləmək - refresh token ilə")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO dto) {

        RefreshToken storedToken = refreshTokenService.findByToken(dto.getRefreshToken());
        refreshTokenService.verifyExpiration(storedToken);

        User user = storedToken.getUser();
        UserPrincipal principal = new UserPrincipal(user);
        String newAccessToken = jwtUtil.generateToken(principal);

        AuthResponseDTO response = new AuthResponseDTO(
                newAccessToken,
                storedToken.getToken(), // eyni refresh token qalır
                user.getEmail(),
                user.getRole().name()
        );

        return ResponseEntity.ok(response);
    }


    //logout ucun api
    @Operation(summary = "Logout - refresh tokeni ləğv etmək")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequestDTO dto) {
        refreshTokenService.revokeToken(dto.getRefreshToken());
        return ResponseEntity.noContent().build();
    }
}