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

class CompanyControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String ADMIN_EMAIL = "admin-company-test@example.com";
    private static final String USER_EMAIL = "user-company-test@example.com";
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
    void createCompany_withUserRole_shouldSucceed() {
        Map<String, Object> requestBody = Map.of(
                "companyName", "Test Company",
                "domain", "testcompany-unique.com",
                "userId", testUserId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/companies", request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("Test Company");
    }

    @Test
    void createCompany_withDuplicateDomain_shouldReturn409() {
        Map<String, Object> requestBody = Map.of(
                "companyName", "First Company",
                "domain", "duplicate-domain.com",
                "userId", testUserId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> firstResponse = restTemplate.postForEntity("/api/companies", request, String.class);
        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Map<String, Object> secondRequestBody = Map.of(
                "companyName", "Second Company",
                "domain", "duplicate-domain.com",
                "userId", testUserId
        );
        HttpEntity<Map<String, Object>> secondRequest = new HttpEntity<>(secondRequestBody, headers);

        ResponseEntity<String> secondResponse = restTemplate.postForEntity("/api/companies", secondRequest, String.class);

        System.out.println("Status: " + secondResponse.getStatusCode());
        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void createCompany_withoutToken_shouldReturn401() {
        Map<String, Object> requestBody = Map.of(
                "companyName", "No Auth Company",
                "domain", "noauth-company.com",
                "userId", testUserId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/companies", request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getAllCompanies_withValidToken_shouldSucceed() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/companies", HttpMethod.GET, request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteCompany_withUserRole_shouldReturn403() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/companies/1", HttpMethod.DELETE, request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void deleteCompany_withAdminRole_nonExistentId_shouldReturn404() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, adminCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/companies/99999", HttpMethod.DELETE, request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}