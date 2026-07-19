package com.example.Excermol.security.jwt;

import com.example.Excermol.security.userdetails.CustomUserDetailsService;
import com.example.Excermol.security.userdetails.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//JwtAuthenticationFilter OncePerRequestFilter-dən extend edir ki,
// hər HTTP request üçün filter yalnız bir dəfə icra olunsun.
// Beləliklə JWT token yalnız bir dəfə yoxlanılır, SecurityContextHolder bir dəfə doldurulur
// və eyni request zamanı filterin təkrar işləməsinin qarşısı alınır.
// Bu həm performans, həm də düzgün autentifikasiya baxımından vacib olur.
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Header yoxdursa və ya "Bearer " ilə başlamırsa, sadəcə keç
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7); // "Bearer " sözünü çıxarırıq
        final String userEmail;

        try {
            userEmail = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            // Token pozulubsa və ya səhvdirsə, sadəcə keç (autentifikasiya olunmayacaq)
            filterChain.doFilter(request, response);
            return;
        }

//        SecurityContextHolder Spring Security-də cari request üçün autentifikasiya məlumatlarını saxlayan komponentdir.
//        JWT və ya login uğurla yoxlanıldıqdan sonra Authentication obyekti SecurityContextHolder-ə yerləşdirilir.
//        Daha sonra controller, service və digər komponentlər buradan login olmuş istifadəçini (Principal),
//        onun rolunu (Authorities) və autentifikasiya vəziyyətini əldə edə bilirlər.
//        Request tamamlandıqda isə bu məlumat Spring tərəfindən təmizlənir.

        // Əgər email tapıldısa VƏ hələ heç kim autentifikasiya olunmayıbsa
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if (jwtUtil.isTokenValid(jwt, (UserPrincipal) userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
