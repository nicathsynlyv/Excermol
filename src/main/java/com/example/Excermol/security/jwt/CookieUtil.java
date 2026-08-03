package com.example.Excermol.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public static final String ACCESS_TOKEN_COOKIE = "accessToken";
    public static final String REFRESH_TOKEN_COOKIE = "refreshToken";

    @Value("${app.cookie.secure:false}")
    private boolean secureCookie;


//    rauzerə "bu məlumatı saxla" deyir (access/refresh token-ləri buraya yazacağıq)
    public void addCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);   // JavaScript bu cookie-ni oxuya bilməz (XSS-dən qorunma)
        cookie.setSecure(secureCookie);    // Development üçün false, production-da HTTPS ilə true olmalıd,indi true elemek yerine hem locaklda hemde serverde islek etdim
        cookie.setPath("/");        // Bütün path-lar üçün etibarlıdır
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }
//    brauzerin göndərdiyi cookie-dən dəyəri oxuyur
//    (JwtAuthenticationFilter bunu istifadə edəcək, token-i header əvəzinə cookie-dən oxumaq üçün)
    public String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }
//    logout zamanı cookie-ni silmək
    public void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookie); // əlavə et
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}