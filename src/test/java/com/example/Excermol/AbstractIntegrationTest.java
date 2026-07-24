//package com.example.Excermol;
//
//import com.example.Excermol.entity.User;
//import com.example.Excermol.enums.UserRole;
//import com.example.Excermol.enums.UserStatus;
//import com.example.Excermol.repository.RefreshTokenRepository;
//import com.example.Excermol.repository.UserRepository;
//import org.junit.jupiter.api.BeforeAll;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.*;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.util.List;
//import java.util.Map;
//
//@Testcontainers  //JUnit 5-ə deyir ki, "bu class-da Testcontainers istifadə olunur, onları idarə et"
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)   //Bu, tam Spring Boot tətbiqini real bir HTTP portunda başladır (təsadüfi port seçərək, ki, əgər sənin development server-in də işə düşübsə, konflikt olmasın). Bu, bizə real HTTP sorğuları göndərməyə imkan verəcək (TestRestTemplate və ya MockMvc ilə)
//public abstract class AbstractIntegrationTest {
//
////    1.static final PostgreSQLContainer — static olması vacibdir, çünki bu container-in bütün test class-ları arasında paylaşılmasına imkan verir (hər dəfə yeni container yaratmaq əvəzinə)
////    2.postgres:16-alpine — yüngül, sürətli bir PostgreSQL image-i
//    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
//            .withDatabaseName("excermol_test")
//            .withUsername("test_user")
//            .withPassword("test_pass");
//
////    @BeforeAll startContainer() — testlər başlamazdan əvvəl, Docker container-ini işə salır
//    @BeforeAll
//    static void startContainer() {
//        postgres.start();
//    }
//
////    1.@DynamicPropertySource — bu, çox vacib bir hissədir: container təsadüfi bir portda işə düşür
////    1.(məsələn 54321), biz bunu əvvəlcədən bilmirik. Bu metod, runtime-da container-in real JDBC URL-ini,
////    1.istifadəçi adını, parolunu götürüb, Spring-in application.properties-indəki dəyərlərin yerinə qoyur
////    2.ddl-auto=create-drop — hər test session-unda, DB sxemi sıfırdan yaradılır, test bitəndə silinir (təmiz, sıfırdan başlanğıc təmin edir)
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
//    }
//
////    Spring Boot-un test üçün verdiyi, real HTTP sorğuları göndərən bir alət (@SpringBootTest(webEnvironment = RANDOM_PORT) ilə birlikdə işləyir)
//    @Autowired
//    protected TestRestTemplate restTemplate;
//
//    @Autowired
//    protected UserRepository userRepository;
//
//    @Autowired
//    protected PasswordEncoder passwordEncoder;
//
//    @Autowired
//    protected RefreshTokenRepository  refreshTokenRepository;
//
//    // Test üçün bir user yaradır (DB-yə birbaşa)
////    createTestUser — hər test öz test user-ini yaratmaq üçün bu metodu çağıracaq (DB birbaşa, /auth/register-dən keçmədən — daha sürətli və sadədir)
//    protected User createTestUser(String email, String rawPassword, UserRole role) {
//        User user = new User();
//        user.setFullName("Test User");
//        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(rawPassword));
//        user.setRole(role);
//        user.setStatus(UserStatus.ACTIVE);
//        return userRepository.save(user);
//    }
//
//    // Login edir, access token cookie-sini qaytarır (sonrakı sorğularda istifadə üçün)
////    real /auth/login endpoint-ini çağırır, response-dan Set-Cookie header-ini oxuyub, accessToken=... sətrini çıxarır. Bunu sonra sorğularımızda Cookie header-i kimi göndərəcəyik
//    protected String loginAndGetAccessTokenCookie(String email, String rawPassword) {
//
//        Map<String, String> loginRequest = Map.of("email", email, "password", rawPassword);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(loginRequest, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity("/auth/login", requestEntity, String.class);
//
//        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
//        if (cookies == null) {
//            throw new IllegalStateException("Login uğursuz oldu, cookie qaytarılmadı");
//        }
//
//        return cookies.stream()
//                .filter(c -> c.startsWith("accessToken="))
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException("accessToken cookie tapılmadı"));
//    }
//
//    protected void cleanupTestUser(String email) {
//        userRepository.findByEmail(email).ifPresent(user -> {
//            refreshTokenRepository.deleteAll(
//                    refreshTokenRepository.findAllByUserIdAndRevokedFalse(user.getId())
//            );
//            userRepository.delete(user);
//        });
//    }
//}
