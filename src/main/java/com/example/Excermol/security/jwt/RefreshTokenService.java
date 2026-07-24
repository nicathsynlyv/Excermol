package com.example.Excermol.security.jwt;


import com.example.Excermol.entity.RefreshToken;
import com.example.Excermol.entity.User;
import com.example.Excermol.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;


    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationMs;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // Yeni refresh token yarat və DB-də saxla
    //    createRefreshToken — hər login-də çağırılacaq,
    //    UUID.randomUUID() ilə təsadüfi, unikal bir string yaradır
    //    bu, JWT deyil, sadəcə uzun təsadüfi bir identifikator — çünki DB-də saxlanılır və müqayisə olunur
    //    "self-contained" olmasına ehtiyac yoxdur
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000));
        refreshToken.setRevoked(false);

        RefreshToken saved = refreshTokenRepository.save(refreshToken);
        log.info("Refresh token created for user: {}", user.getEmail());
        return saved;
    }

    // Token-i tap və etibarlılığını yoxla
    //    verifyExpiration — /auth/refresh çağrılanda,
    //    gələn refresh token-in ləğv olunmadığını və vaxtının bitmədiyini yoxlayır
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isRevoked()) {
            log.warn("Refresh token is revoked for user: {}", token.getUser().getEmail());
            throw new RefreshTokenException("Refresh token ləğv edilib. Yenidən daxil olun");
        }

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Refresh token expired for user: {}", token.getUser().getEmail());
            throw new RefreshTokenException("Refresh token-in vaxtı bitib. Yenidən daxil olun");
        }

        return token;
    }

    // RefreshTokenService.java-ya əlavə et

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Refresh token tapılmadı"));
    }


    // logout ucun metod
    @Transactional
    public void revokeToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Refresh token tapılmadı"));

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
        log.info("Refresh token revoked for user: {}", refreshToken.getUser().getEmail());
    }

    // Logout all ucun metod
    @Transactional
    public void revokeAllUserTokens(Long userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
        log.info("All refresh tokens revoked for user id: {}", userId);
    }

}
