//package com.example.Excermol.controller;
//
//import com.example.Excermol.AbstractIntegrationTest;
//import com.example.Excermol.enums.UserRole;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.*;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class AuthControllerIntegrationTest extends AbstractIntegrationTest {
//
//    private static final String TEST_EMAIL = "auth-test@example.com";
//    private static final String TEST_PASSWORD = "password123";
//
//    @AfterEach
//    void tearDown() {
//        cleanupTestUser(TEST_EMAIL);
//        cleanupTestUser("register-test@example.com");
//    }
//
//    @Test
//    void login_withValidCredentials_shouldReturnCookiesAndUserInfo() {
//        createTestUser(TEST_EMAIL, TEST_PASSWORD, UserRole.USER);
//
//        Map<String, String> loginRequest = Map.of("email", TEST_EMAIL, "password", TEST_PASSWORD);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(loginRequest, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity("/auth/login", request, String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).contains(TEST_EMAIL);
//        assertThat(response.getBody()).contains("USER");
//
//        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
//        assertThat(cookies).isNotNull();
//        assertThat(cookies.stream().anyMatch(c -> c.startsWith("accessToken="))).isTrue();
//        assertThat(cookies.stream().anyMatch(c -> c.startsWith("refreshToken="))).isTrue();
//    }
//
//    @Test
//    void login_withWrongPassword_shouldReturn401() {
//        createTestUser(TEST_EMAIL, TEST_PASSWORD, UserRole.USER);
//
//        Map<String, String> loginRequest = Map.of("email", TEST_EMAIL, "password", "wrongpassword");
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(loginRequest, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity("/auth/login", request, String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
//    }
//
//    @Test
//    void login_withNonExistentEmail_shouldReturn401() {
//        Map<String, String> loginRequest = Map.of("email", "nonexistent@example.com", "password", "anypassword");
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(loginRequest, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity("/auth/login", request, String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
//    }
//
//    @Test
//    void register_withNewEmail_shouldCreateUserAndReturnCookies() {
//        Map<String, String> registerRequest = Map.of(
//                "fullName", "Test Register",
//                "email", "register-test@example.com",
//                "password", "password123"
//        );
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(registerRequest, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity("/auth/register", request, String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(response.getBody()).contains("register-test@example.com");
//        assertThat(response.getBody()).contains("USER"); // register həmişə USER rolu verir
//
//        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
//        assertThat(cookies).isNotNull();
//    }
//
//    @Test
//    void register_withDuplicateEmail_shouldReturn409() {
//        createTestUser(TEST_EMAIL, TEST_PASSWORD, UserRole.USER);
//
//        Map<String, String> registerRequest = Map.of(
//                "fullName", "Duplicate User",
//                "email", TEST_EMAIL,
//                "password", "password123"
//        );
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(registerRequest, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity("/auth/register", request, String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
//    }
//
//    @Test
//    void refresh_withValidRefreshToken_shouldReturnNewAccessToken() {
//        createTestUser(TEST_EMAIL, TEST_PASSWORD, UserRole.USER);
//        String accessTokenCookie = loginAndGetAccessTokenCookie(TEST_EMAIL, TEST_PASSWORD);
//
//        // login-dən sonra bütün cookie-ləri əldə etmək üçün, ayrıca login sorğusu göndəririk
//        Map<String, String> loginRequest = Map.of("email", TEST_EMAIL, "password", TEST_PASSWORD);
//        HttpHeaders loginHeaders = new HttpHeaders();
//        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, String>> loginEntity = new HttpEntity<>(loginRequest, loginHeaders);
//        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/auth/login", loginEntity, String.class);
//
//        String refreshCookie = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE).stream()
//                .filter(c -> c.startsWith("refreshToken="))
//                .findFirst()
//                .orElseThrow();
//
//        HttpHeaders refreshHeaders = new HttpHeaders();
//        refreshHeaders.add(HttpHeaders.COOKIE, refreshCookie);
//        HttpEntity<Void> refreshRequest = new HttpEntity<>(refreshHeaders);
//
//        ResponseEntity<String> refreshResponse = restTemplate.postForEntity("/auth/refresh", refreshRequest, String.class);
//
//        assertThat(refreshResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        List<String> newCookies = refreshResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
//        assertThat(newCookies).isNotNull();
//        assertThat(newCookies.stream().anyMatch(c -> c.startsWith("accessToken="))).isTrue();
//    }
//
//    @Test
//    void refresh_withoutCookie_shouldReturn401() {
//        ResponseEntity<String> response = restTemplate.postForEntity("/auth/refresh", null, String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
//    }
//
//    @Test
//    void logout_shouldRevokeTokenAndClearCookies() {
//        createTestUser(TEST_EMAIL, TEST_PASSWORD, UserRole.USER);
//
//        Map<String, String> loginRequest = Map.of("email", TEST_EMAIL, "password", TEST_PASSWORD);
//        HttpHeaders loginHeaders = new HttpHeaders();
//        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, String>> loginEntity = new HttpEntity<>(loginRequest, loginHeaders);
//        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/auth/login", loginEntity, String.class);
//
//        String refreshCookie = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE).stream()
//                .filter(c -> c.startsWith("refreshToken="))
//                .findFirst()
//                .orElseThrow();
//
//        HttpHeaders logoutHeaders = new HttpHeaders();
//        logoutHeaders.add(HttpHeaders.COOKIE, refreshCookie);
//        HttpEntity<Void> logoutRequest = new HttpEntity<>(logoutHeaders);
//
//        ResponseEntity<Void> logoutResponse = restTemplate.postForEntity("/auth/logout", logoutRequest, Void.class);
//
//        assertThat(logoutResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//
//        // İndi eyni refresh token ilə yenidən refresh cəhdi et - rədd olunmalıdır
//        HttpHeaders retryHeaders = new HttpHeaders();
//        retryHeaders.add(HttpHeaders.COOKIE, refreshCookie);
//        HttpEntity<Void> retryRequest = new HttpEntity<>(retryHeaders);
//
//        ResponseEntity<String> retryResponse = restTemplate.postForEntity("/auth/refresh", retryRequest, String.class);
//
//        assertThat(retryResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
//    }
//}