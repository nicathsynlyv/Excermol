package com.example.Excermol.controller;

import com.example.Excermol.AbstractIntegrationTest;
import com.example.Excermol.entity.Campaign;
import com.example.Excermol.entity.User;
import com.example.Excermol.enums.CampaignStatus;
import com.example.Excermol.enums.UserRole;
import com.example.Excermol.repository.CampaignLeadRepository;
import com.example.Excermol.repository.CampaignRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CampaignLeadControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String ADMIN_EMAIL = "admin-lead-test@example.com";
    private static final String USER_EMAIL = "user-lead-test@example.com";
    private static final String PASSWORD = "password123";

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignLeadRepository campaignLeadRepository;

    private String adminCookie;
    private String userCookie;
    private Long testCampaignId;

    @BeforeEach
    void setUp() {
        createTestUser(ADMIN_EMAIL, PASSWORD, UserRole.ADMIN);
        User testUser = createTestUser(USER_EMAIL, PASSWORD, UserRole.USER);

        adminCookie = loginAndGetAccessTokenCookie(ADMIN_EMAIL, PASSWORD);
        userCookie = loginAndGetAccessTokenCookie(USER_EMAIL, PASSWORD);

        Campaign campaign = new Campaign();
        campaign.setName("Lead Test Campaign");
        campaign.setStatus(CampaignStatus.ACTIVE);
        campaign.setUser(testUser);
        testCampaignId = campaignRepository.save(campaign).getId();
    }

    @AfterEach
    void tearDown() {
        campaignLeadRepository.deleteAll(campaignLeadRepository.findByCampaignId(testCampaignId));
        campaignRepository.deleteById(testCampaignId);
        cleanupTestUser(ADMIN_EMAIL);
        cleanupTestUser(USER_EMAIL);
    }

    @Test
    void createLead_withUserRole_shouldSucceed() {
        Map<String, Object> requestBody = Map.of(
                "leadName", "Test Lead",
                "leadEmail", "testlead@example.com",
                "campaignId", testCampaignId,
                "status", "OPENED"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/leads", request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("Test Lead");
    }

    @Test
    void createLead_withInvalidEmail_shouldReturn400() {
        Map<String, Object> requestBody = Map.of(
                "leadName", "Invalid Email Lead",
                "leadEmail", "not-an-email",
                "campaignId", testCampaignId,
                "status", "OPENED"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/leads", request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createLead_withoutToken_shouldReturn401() {
        Map<String, Object> requestBody = Map.of(
                "leadName", "No Auth Lead",
                "leadEmail", "noauth-lead@example.com",
                "campaignId", testCampaignId,
                "status", "OPENED"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/leads", request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getLeadsByCampaign_shouldSucceed() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/leads/campaign/" + testCampaignId, HttpMethod.GET, request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteLead_withUserRole_shouldReturn403() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, userCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/leads/1", HttpMethod.DELETE, request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void deleteLead_withAdminRole_nonExistentId_shouldReturn404() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, adminCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/leads/99999", HttpMethod.DELETE, request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}