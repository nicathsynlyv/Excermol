package com.example.Excermol.security.jwt;

import com.example.Excermol.security.userdetails.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

//token yaratma/oxuma məntiqi
//JwtUtil JWT ilə işləyən bütün əməliyyatları bir yerdə toplayan köməkçi (utility) classdır.
// O, istifadəçi uğurla login olduqda token yaradır (generateToken()), gələn tokeni oxuyur (extractAllClaims())
// içindən lazım olan məlumatları (email, bitmə tarixi və s.) çıxarır (extractUsername(), extractExpiration())
// və tokenin həm imzasını, həm də istifadə müddətini yoxlayaraq etibarlı olub-olmadığını müəyyən edir (isTokenValid()).
// Bu sayədə tətbiqin digər hissələri JWT-nin daxili detallarını bilmədən sadəcə bu metodlardan istifadə edə bilir.
@Component
public class JwtUtil {

//    Bu açar, token-in "imzalanması" üçün lazımdır
//    yəni heç kim bu açarı bilmədən saxta token yarada bilməz
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Token yaratmaq
    public String generateToken(UserPrincipal userPrincipal) {
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("userId", userPrincipal.getId())
                .claim("role", userPrincipal.getUser().getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    // Token içindən istifadəçinin email-ni çıxarır
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Token-dən bitmə tarixini çıxar
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Ümumi claim çıxarma metodu
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Token etibarlıdırmı yoxluyur ve username i yeni emaili eyni oldugunu yoxluyur
    public boolean isTokenValid(String token, UserPrincipal userPrincipal) {
        String username = extractUsername(token);
        return username.equals(userPrincipal.getUsername()) && !isTokenExpired(token);
    }

    // Token vaxdin bitib bitmemesini yoxluyur
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
