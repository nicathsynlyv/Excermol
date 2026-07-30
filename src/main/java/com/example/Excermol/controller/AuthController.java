package com.example.Excermol.controller;

import com.example.Excermol.Service.UserService;
import com.example.Excermol.entity.RefreshToken;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.*;
import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import com.example.Excermol.security.jwt.*;
import com.example.Excermol.security.userdetails.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private static final int ACCESS_TOKEN_MAX_AGE = 15 * 60;
    private static final int REFRESH_TOKEN_MAX_AGE = 30 * 24 * 60 * 60;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;
    private final PasswordResetService passwordResetService;
    private final RateLimitingService rateLimitingService;


    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserService userService,
                          RefreshTokenService refreshTokenService,
                          CookieUtil cookieUtil, PasswordResetService passwordResetService, RateLimitingService rateLimitingService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.cookieUtil = cookieUtil;
        this.passwordResetService = passwordResetService;
        this.rateLimitingService = rateLimitingService;
    }

    @Operation(summary = "Login - email və şifrə ilə giriş")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto,
                                                 HttpServletResponse response,
                                                 HttpServletRequest request) {

        String clientIp = getClientIp(request);
        rateLimitingService.checkRateLimit(dto.getEmail(), clientIp);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            String accessToken = jwtUtil.generateToken(principal);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(principal.getUser());

            cookieUtil.addCookie(response, CookieUtil.ACCESS_TOKEN_COOKIE, accessToken, ACCESS_TOKEN_MAX_AGE);
            cookieUtil.addCookie(response, CookieUtil.REFRESH_TOKEN_COOKIE, refreshToken.getToken(), REFRESH_TOKEN_MAX_AGE);

            rateLimitingService.resetAttempts(dto.getEmail(), clientIp);

            AuthResponseDTO responseBody = new AuthResponseDTO(
                    principal.getUsername(),
                    principal.getUser().getRole().name()
            );

            return ResponseEntity.ok(responseBody);

        } catch (org.springframework.security.core.AuthenticationException ex) {
            rateLimitingService.recordFailedAttempt(dto.getEmail(), clientIp);
            throw ex;
        }
    }

    // IP ünvanını əldə etmək üçün köməkçi metod
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @Operation(summary = "Qeydiyyat - yeni istifadəçi yaratmaq")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto,
                                                    HttpServletResponse response) {

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

        cookieUtil.addCookie(response, CookieUtil.ACCESS_TOKEN_COOKIE, accessToken, ACCESS_TOKEN_MAX_AGE);
        cookieUtil.addCookie(response, CookieUtil.REFRESH_TOKEN_COOKIE, refreshToken.getToken(), REFRESH_TOKEN_MAX_AGE);

        AuthResponseDTO responseBody = new AuthResponseDTO(
                principal.getUsername(),
                principal.getUser().getRole().name()
        );

        return ResponseEntity.status(201).body(responseBody);
    }

    @Operation(summary = "Access tokeni yeniləmək - refresh token ilə")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(HttpServletRequest request,
                                                        HttpServletResponse response) {

        String refreshTokenValue = cookieUtil.getCookieValue(request, CookieUtil.REFRESH_TOKEN_COOKIE);
        if (refreshTokenValue == null) {
            throw new com.example.Excermol.security.jwt.RefreshTokenException("Refresh token tapılmadı");
        }

        RefreshToken storedToken = refreshTokenService.findByToken(refreshTokenValue);
        refreshTokenService.verifyExpiration(storedToken);

        User user = storedToken.getUser();
        UserPrincipal principal = new UserPrincipal(user);
        String newAccessToken = jwtUtil.generateToken(principal);

        cookieUtil.addCookie(response, CookieUtil.ACCESS_TOKEN_COOKIE, newAccessToken, ACCESS_TOKEN_MAX_AGE);

        AuthResponseDTO responseBody = new AuthResponseDTO(
                user.getEmail(),
                user.getRole().name()
        );

        return ResponseEntity.ok(responseBody);
    }

    @Operation(summary = "Logout - refresh tokeni ləğv etmək")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request,
                                       HttpServletResponse response) {

        String refreshTokenValue = cookieUtil.getCookieValue(request, CookieUtil.REFRESH_TOKEN_COOKIE);
        if (refreshTokenValue != null) {
            refreshTokenService.revokeToken(refreshTokenValue);
        }

        cookieUtil.deleteCookie(response, CookieUtil.ACCESS_TOKEN_COOKIE);
        cookieUtil.deleteCookie(response, CookieUtil.REFRESH_TOKEN_COOKIE);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Bütün cihazlardan çıxış")
    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(Authentication authentication,
                                          HttpServletResponse response) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        refreshTokenService.revokeAllUserTokens(principal.getId());

        cookieUtil.deleteCookie(response, CookieUtil.ACCESS_TOKEN_COOKIE);
        cookieUtil.deleteCookie(response, CookieUtil.REFRESH_TOKEN_COOKIE);

        return ResponseEntity.noContent().build();
    }

    //forget password ucun api-lar
    @Operation(summary = "Şifrə bərpası - OTP kodu email-ə göndər")
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO dto) {
        passwordResetService.requestPasswordReset(dto.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "OTP kodunu doğrula")
    @PostMapping("/verify-otp")
    public ResponseEntity<Void> verifyOtp(@Valid @RequestBody VerifyOtpRequestDTO dto) {
        passwordResetService.verifyOtp(dto.getEmail(), dto.getOtpCode());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Yeni şifrə təyin et")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO dto) {
        passwordResetService.resetPassword(dto.getEmail(), dto.getOtpCode(), dto.getNewPassword());
        return ResponseEntity.ok().build();
    }
}