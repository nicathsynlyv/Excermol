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

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final CookieUtil cookieUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   CustomUserDetailsService userDetailsService,
                                   CookieUtil cookieUtil) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.cookieUtil = cookieUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = extractToken(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String userEmail;

        try {
            userEmail = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

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

    // Əvvəlcə cookie-dən, tapılmasa header-dən oxuyur (geriyə uyğunluq üçün)
    //    extractToken(request) — yeni bir private metod, bu, token-i əvvəlcə cookie-dən, tapılmasa header-dən axtarır.
    //    Bu, "hər ikisini dəstəklə" prinsipinə uyğundur — Postman-da həm cookie, həm header ilə test edə bilərsən, ikisi də işləyəcək
    private String extractToken(HttpServletRequest request) {
        String tokenFromCookie = cookieUtil.getCookieValue(request, CookieUtil.ACCESS_TOKEN_COOKIE);
        if (tokenFromCookie != null) {
            return tokenFromCookie;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}