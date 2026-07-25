package com.example.Excermol.controller;

import com.example.Excermol.AbstractIntegrationTest;
import com.example.Excermol.entity.Company;
import com.example.Excermol.entity.User;
import com.example.Excermol.enums.CompanyStatus;
import com.example.Excermol.enums.UserRole;
import com.example.Excermol.repository.CompanyRepository;
import com.example.Excermol.repository.PipelineRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PipelineControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String ADMIN_EMAIL = "admin-pipeline-test@example.com";
    private static final String USER_EMAIL = "user-pipeline-test@example.com";
    private static final String PASSWORD = "password123";

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PipelineRepository pipelineRepository;

    private String adminCookie;
    private String userCookie;
    private Long testCompanyId;

    @BeforeEach
    void setUp() {
        createTestUser(ADMIN_EMAIL, PASSWORD, UserRole.ADMIN);
        User testUser = createTestUser(USER_EMAIL, PASSWORD, UserRole.USER);

        adminCookie = loginAndGetAccessTokenCookie(ADMIN_EMAIL, PASSWORD);
        userCookie = loginAndGetAccessTokenCookie(USER_EMAIL, PASSWORD);

        Company company = new Company();
        company.setCompanyName("Pipeline Test Company");
        company.setDomain("pipeline-test-company.com");
        company.setStatus(CompanyStatus.INTERESTED);
        testCompanyId = companyRepository.save(company).getId();
    }

    @AfterEach
    void tearDown() {
        pipelineRepository.deleteAll(pipelineRepository.findByCompanyId(testCompanyId));
        companyRepository.deleteById(testCompanyId);
        cleanupTestUser(ADMIN_EMAIL);
        cleanupTestUser(USER_EMAIL);
    }

    @Test
    void createPipeline_withUserRole_shouldSucceed() {
        Map<String, Object> requestBody = Map.of(
                "name", "Test Pipeline",
                "status", "AGENCY",
                "stage", "WARM",
                "companyId", testCompanyId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/pipelines", request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("Test Pipeline");
    }

    @Test
    void createPipeline_withNonExistentCompany_shouldReturn404() {
        Map<String, Object> requestBody = Map.of(
                "name", "Orphan Pipeline",
                "status", "AGENCY",
                "stage", "WARM",
                "companyId", 999999L
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/pipelines", request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createPipeline_withoutToken_shouldReturn401() {
        Map<String, Object> requestBody = Map.of(
                "name", "No Auth Pipeline",
                "status", "AGENCY",
                "stage", "WARM",
                "companyId", testCompanyId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/pipelines", request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getAllPipelines_withValidToken_shouldSucceed() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/pipelines", HttpMethod.GET, request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deletePipeline_withUserRole_shouldReturn403() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/pipelines/1", HttpMethod.DELETE, request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getPipelineById_withNonExistentId_shouldReturn404() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/pipelines/99999", HttpMethod.GET, request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}