package com.example.Excermol.controller;

import com.example.Excermol.AbstractIntegrationTest;
import com.example.Excermol.enums.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String ADMIN_EMAIL = "admin-user-test@example.com";
    private static final String USER_EMAIL = "user-user-test@example.com";
    private static final String NEW_USER_EMAIL = "newuser-test@example.com";
    private static final String PASSWORD = "password123";

    private String adminCookie;
    private String userCookie;

    @BeforeEach
    void setUp() {
        createTestUser(ADMIN_EMAIL, PASSWORD, UserRole.ADMIN);
        createTestUser(USER_EMAIL, PASSWORD, UserRole.USER);

        adminCookie = loginAndGetAccessTokenCookie(ADMIN_EMAIL, PASSWORD);
        userCookie = loginAndGetAccessTokenCookie(USER_EMAIL, PASSWORD);
    }

    @AfterEach
    void tearDown() {
        cleanupTestUser(ADMIN_EMAIL);
        cleanupTestUser(USER_EMAIL);
        cleanupTestUser(NEW_USER_EMAIL);
    }

    @Test
    void getAllUsers_withAdminRole_shouldSucceed() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, adminCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/users/all", HttpMethod.GET, request, String.class);

        System.out.println("Status: " + response.getStatusCode());


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAllUsers_withUserRole_shouldReturn403() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/users/all", HttpMethod.GET, request, String.class);


        System.out.println("Status: " + response.getStatusCode());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getAllUsers_withoutToken_shouldReturn401() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/users/all", HttpMethod.GET, HttpEntity.EMPTY, String.class);


        System.out.println("Status: " + response.getStatusCode());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void createUser_withAdminRole_shouldSucceed() {
        Map<String, Object> requestBody = Map.of(
                "fullName", "New Test User",
                "email", NEW_USER_EMAIL,
                "password", "password123",
                "role", "USER",
                "status", "ACTIVE"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, adminCookie);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/users", request, String.class);

        System.out.println("Status: " + response.getStatusCode());


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains(NEW_USER_EMAIL);
    }

    @Test
    void createUser_withUserRole_shouldReturn403() {
        Map<String, Object> requestBody = Map.of(
                "fullName", "Hacker Attempt",
                "email", "hacker-test@example.com",
                "password", "password123",
                "role", "ADMIN", // USER özünə ADMIN vermeye calisir
                "status", "ACTIVE"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/users", request, String.class);


        System.out.println("Status: " + response.getStatusCode());


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void deleteUser_withUserRole_shouldReturn403() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/users/1", HttpMethod.DELETE, request, String.class);

        System.out.println("Status: " + response.getStatusCode());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getUsersByRole_withAdminRole_shouldSucceed() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, adminCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/users/by-role?role=USER", HttpMethod.GET, request, String.class);


        System.out.println("Status: " + response.getStatusCode());


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}