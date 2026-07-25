package com.example.Excermol.repository;

import com.example.Excermol.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

//    "bu istifadəçi üçün, hələ istifadə olunmamış,
//    ən son yaradılmış token-i tap". Bu, vacibdir,
//    çünki istifadəçi bir neçə dəfə "Forgot Password" düyməsinə bassa
//    (yeni OTP istəsə), biz ən son yaradılan OTP-ni yoxlamalıyıq, köhnəsini yox
    Optional<PasswordResetToken> findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(Long userId);
}