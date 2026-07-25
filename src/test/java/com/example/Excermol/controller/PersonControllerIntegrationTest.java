package com.example.Excermol.controller;

import com.example.Excermol.AbstractIntegrationTest;
import com.example.Excermol.entity.User;
import com.example.Excermol.enums.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PersonControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String ADMIN_EMAIL = "admin-person-test@example.com";
    private static final String USER_EMAIL = "user-person-test@example.com";
    private static final String PASSWORD = "password123";

    private String adminCookie;
    private String userCookie;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        createTestUser(ADMIN_EMAIL, PASSWORD, UserRole.ADMIN);
        User testUser = createTestUser(USER_EMAIL, PASSWORD, UserRole.USER);
        testUserId = testUser.getId();

        adminCookie = loginAndGetAccessTokenCookie(ADMIN_EMAIL, PASSWORD);
        userCookie = loginAndGetAccessTokenCookie(USER_EMAIL, PASSWORD);
    }

    @AfterEach
    void tearDown() {
        cleanupTestUser(ADMIN_EMAIL);
        cleanupTestUser(USER_EMAIL);
    }

    @Test
    void createPerson_withUserRole_shouldSucceed() {
        Map<String, Object> requestBody = Map.of(
                "fullName", "John",
                "lastName", "Doe",
                "email", "john.doe.test@example.com",
                "status", "ENGAGED",
                "userId", testUserId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/people", request, String.class);
        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("John");
    }

    @Test
    void createPerson_withMissingRequiredField_shouldReturn400() {
        Map<String, Object> requestBody = Map.of(
                "fullName", "John",
                // lastName əskikdir - @NotBlank tələb edir
                "email", "incomplete-test@example.com",
                "status", "ENGAGED",
                "userId", testUserId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/people", request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createPerson_withInvalidEmail_shouldReturn400() {
        Map<String, Object> requestBody = Map.of(
                "fullName", "John",
                "lastName", "Doe",
                "email", "not-a-valid-email",
                "status", "ENGAGED",
                "userId", testUserId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/people", request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createPerson_withoutToken_shouldReturn401() {
        Map<String, Object> requestBody = Map.of(
                "fullName", "John",
                "lastName", "Doe",
                "email", "noauth-person-test@example.com",
                "status", "ENGAGED",
                "userId", testUserId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/people", request, String.class);


        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getAllPersons_withValidToken_shouldSucceed() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/people", HttpMethod.GET, request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deletePerson_withUserRole_shouldReturn403() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/people/1", HttpMethod.DELETE, request, String.class);


        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getPersonById_withNonExistentId_shouldReturn404() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/people/99999", HttpMethod.GET, request, String.class);



        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}