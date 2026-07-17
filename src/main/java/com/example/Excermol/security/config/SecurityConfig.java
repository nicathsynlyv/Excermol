package com.example.Excermol.security.config;

import com.example.Excermol.security.jwt.CustomAccessDeniedHandler;
import com.example.Excermol.security.jwt.JwtAuthenticationEntryPoint;
import com.example.Excermol.security.jwt.JwtAuthenticationFilter;
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
@EnableMethodSecurity //role based islemek ucun annotasiyalari aktiv edir
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler; // yeni


    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
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
        provider.setPasswordEncoder(passwordEncoder());
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


//    BCryptPasswordEncoder istifadəçilərin şifrələrini təhlükəsiz şəkildə hash etmək üçün istifadə olunur.
//    Şifrələr açıq mətn kimi deyil, hash olunmuş formada databasedə saxlanılır.
//    Login zamanı isə daxil edilən şifrə matches() metodu ilə hash olunmuş dəyərlə müqayisə edilir.
//    BCrypt təsadüfi salt istifadə etdiyi üçün eyni şifrə hər dəfə fərqli hash yaradır
//    və bu da rainbow table və brute-force hücumlarına qarşı daha yüksək təhlükəsizlik təmin edir.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
                                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                                         // /api/users - yalnız ADMIN
                                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                                        .anyRequest().authenticated()
                        )
                        .authenticationProvider(authenticationProvider())
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();

//    evvelki
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