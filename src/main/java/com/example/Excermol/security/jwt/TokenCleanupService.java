package com.example.Excermol.security.jwt;

import com.example.Excermol.repository.PasswordResetTokenRepository;
import com.example.Excermol.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class TokenCleanupService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public TokenCleanupService(RefreshTokenRepository refreshTokenRepository,
                               PasswordResetTokenRepository passwordResetTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    // Hər gün saat 03:00-da işə düşür
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("Starting scheduled token cleanup...");

        LocalDateTime now = LocalDateTime.now();

        int deletedRefreshTokens = refreshTokenRepository.deleteAllByExpiryDateBefore(now);
        log.info("Deleted {} expired refresh tokens", deletedRefreshTokens);

        int deletedResetTokens = passwordResetTokenRepository.deleteAllByExpiryDateBefore(now);
        log.info("Deleted {} expired password reset tokens", deletedResetTokens);
    }
}
