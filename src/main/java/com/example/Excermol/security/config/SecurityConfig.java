package com.example.Excermol.security.config;

import com.example.Excermol.security.jwt.CustomAccessDeniedHandler;
import com.example.Excermol.security.jwt.JwtAuthenticationEntryPoint;
import com.example.Excermol.security.jwt.JwtAuthenticationFilter;
import com.example.Excermol.security.oauth2.CustomOAuth2UserService;
import com.example.Excermol.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.example.Excermol.security.userdetails.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //@EnableMethodSecurity — bu, Spring-ə deyir: "controller metodlarının üstündə @PreAuthorize, @PostAuthorize, @Secured kimi annotasiyalar axtar və onları icra et"
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;// yeni
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final PasswordEncoder passwordEncoder;


    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler, CustomOAuth2UserService customOAuth2UserService, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.passwordEncoder = passwordEncoder;
    }


    //    DaoAuthenticationProvider Spring Security-nin standart autentifikasiya komponentidir.
    //    O, UserDetailsService vasitəsilə istifadəçini databasedən tapır
    //    və PasswordEncoder ilə daxil edilən şifrəni databasedə saxlanılan hash olunmuş şifrə ilə müqayisə edir.
    //    Məlumatlar düzgündürsə Authentication obyekti yaradır və AuthenticationManager-ə qaytarır,
    //    əks halda autentifikasiya xətası (BadCredentialsException və s.) atır.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    //    AuthenticationManager bean-i — bunu Mərhələ 2-də AuthController-in login metodunda istifadə edəcəyik, ona görə indi hazırlayırıq
    //    AuthenticationManager istifadəçinin login məlumatlarını (email və şifrə) yoxlayan əsas komponentdir.
    //    O, AuthenticationProvider vasitəsilə istifadəçini databasedən tapır və şifrəni yoxlayır. Əgər məlumatlar düzgündürsə,
    //    Authentication obyekti qaytarır, səhvdirsə exception atır.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }





    //    SessionCreationPolicy.STATELESS JWT əsaslı autentifikasiyada serverin HTTP Session yaratmaması üçün istifadə olunur.
    //    Bu yanaşmada istifadəçi məlumatı serverdə saxlanılmır, bütün autentifikasiya məlumatı JWT token-də olur.
    //    Hər request-də client tokeni Authorization header-i ilə göndərir, server isə tokeni yoxlayaraq istifadəçini autentifikasiya edir.
    //    Bu yanaşma REST API-lər üçün daha uyğun, daha performanslı və horizontal scaling baxımından daha əlverişlidir.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/oauth2/**","/login/oauth2/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // /api/users - yalnız ADMIN
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 ->oauth2
                        .userInfoEndpoint(userinfo ->userinfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                );


        return http.build();

//    evvelki en birinci hele hec bir confiqurasiya elave etmeden
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()
//                );
//
//        return http.build();
//    }
    }
}