package com.example.Excermol.repository;

import com.example.Excermol.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    // token string-inə görə tapmaq (refresh zamanı istifadə olunacaq)
    Optional<RefreshToken> findByToken(String token);

    // istifadəçinin bütün aktiv (revoke olunmamış) tokenləri
    List<RefreshToken> findAllByUserIdAndRevokedFalse(Long userId);

    // istifadəçinin bütün tokenlərini ləğv et (məs. "bütün cihazlardan çıx" funksiyası üçün)
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user.id = :userId")
    void revokeAllByUserId(@Param("userId") Long userId);
}
