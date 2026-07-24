package com.example.Excermol.security.oauth2;

import com.example.Excermol.entity.RefreshToken;
import com.example.Excermol.security.jwt.CookieUtil;
import com.example.Excermol.security.jwt.JwtUtil;
import com.example.Excermol.security.jwt.RefreshTokenService;
import com.example.Excermol.security.userdetails.UserPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final int ACCESS_TOKEN_MAX_AGE = 15 * 60;
    private static final int REFRESH_TOKEN_MAX_AGE = 30 * 24 * 60 * 60;

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil,
                                              RefreshTokenService refreshTokenService,
                                              CookieUtil cookieUtil) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.cookieUtil = cookieUtil;
    }

//    bura, məhz  mövcud login metodumuzda etdiyimiz eyni işi edir: JwtUtil ilə access token, RefreshTokenService ilə refresh token yaradıb, CookieUtil ilə cookie-yə yazırıq
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        String accessToken = jwtUtil.generateToken(principal);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(principal.getUser());

        cookieUtil.addCookie(response, CookieUtil.ACCESS_TOKEN_COOKIE, accessToken, ACCESS_TOKEN_MAX_AGE);
        cookieUtil.addCookie(response, CookieUtil.REFRESH_TOKEN_COOKIE, refreshToken.getToken(), REFRESH_TOKEN_MAX_AGE);

        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }
}
