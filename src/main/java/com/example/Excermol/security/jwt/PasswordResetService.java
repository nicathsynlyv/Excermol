package com.example.Excermol.security.jwt;

import com.example.Excermol.Service.impl.EmailSenderService;
import com.example.Excermol.entity.PasswordResetToken;
import com.example.Excermol.entity.User;
import com.example.Excermol.exception.UserNotFoundException;
import com.example.Excermol.repository.PasswordResetTokenRepository;
import com.example.Excermol.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Slf4j
public class PasswordResetService {

    private static final int OTP_EXPIRY_MINUTES = 5;

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(PasswordResetTokenRepository passwordResetTokenRepository,
                                UserRepository userRepository,
                                EmailSenderService emailSenderService,
                                PasswordEncoder passwordEncoder) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. Addım: OTP yarat, email göndər
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Password reset requested for non-existent email: {}", email);
                    return new UserNotFoundException("Bu email ilə istifadəçi tapılmadı");
                });

        String otpCode = generateOtp();

        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setOtpCode(otpCode);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        token.setVerified(false);
        token.setUsed(false);
        passwordResetTokenRepository.save(token);

        String subject = "Şifrə Bərpası - Excermol";
        String body = "Sizin şifrə bərpası kodunuz: " + otpCode +
                "\nBu kod " + OTP_EXPIRY_MINUTES + " dəqiqə ərzində etibarlıdır.";

        emailSenderService.sendSimpleEmail(email, subject, body);
        log.info("Password reset OTP sent to: {}", email);
    }

    // 2. Addım: OTP-ni doğrula
    @Transactional
    public void verifyOtp(String email, String otpCode) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Bu email ilə istifadəçi tapılmadı"));

        PasswordResetToken token = passwordResetTokenRepository
                .findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(user.getId())
                .orElseThrow(() -> new PasswordResetException("Bu email üçün aktiv kod tapılmadı"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Expired OTP attempt for email: {}", email);
            throw new PasswordResetException("Kodun vaxtı bitib. Yenidən tələb edin");
        }

        if (!token.getOtpCode().equals(otpCode)) {
            log.warn("Invalid OTP attempt for email: {}", email);
            throw new PasswordResetException("Kod yanlışdır");
        }

        token.setVerified(true);
        passwordResetTokenRepository.save(token);
        log.info("OTP verified successfully for: {}", email);
    }

    // 3. Addım: Parolu yenilə
    @Transactional
    public void resetPassword(String email, String otpCode, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Bu email ilə istifadəçi tapılmadı"));

        PasswordResetToken token = passwordResetTokenRepository
                .findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(user.getId())
                .orElseThrow(() -> new PasswordResetException("Bu email üçün aktiv kod tapılmadı"));

        if (!token.isVerified()) {
            log.warn("Password reset attempted without OTP verification for: {}", email);
            throw new PasswordResetException("Əvvəlcə kodu təsdiqləyin");
        }

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new PasswordResetException("Kodun vaxtı bitib. Yenidən tələb edin");
        }

        if (!token.getOtpCode().equals(otpCode)) {
            throw new PasswordResetException("Kod yanlışdır");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        token.setUsed(true);
        passwordResetTokenRepository.save(token);

        log.info("Password reset successfully for: {}", email);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 1000 + random.nextInt(9000); // 1000-9999 arası, 4 rəqəm
        return String.valueOf(otp);
    }
}