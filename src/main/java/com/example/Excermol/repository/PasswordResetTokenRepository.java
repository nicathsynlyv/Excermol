package com.example.Excermol.repository;

import com.example.Excermol.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

//    "bu istifadəçi üçün, hələ istifadə olunmamış,
//    ən son yaradılmış token-i tap". Bu, vacibdir,
//    çünki istifadəçi bir neçə dəfə "Forgot Password" düyməsinə bassa
//    (yeni OTP istəsə), biz ən son yaradılan OTP-ni yoxlamalıyıq, köhnəsini yox
    Optional<PasswordResetToken> findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(Long userId);


//    databaseden vaxdi cixmis otp silir
    @Modifying
    @Query("DELETE FROM PasswordResetToken prt WHERE prt.expiryDate < :now")
    int deleteAllByExpiryDateBefore(@Param("now") LocalDateTime now);
}