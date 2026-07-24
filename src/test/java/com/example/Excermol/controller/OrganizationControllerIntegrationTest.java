//package com.example.Excermol.controller;
//
//
//import com.example.Excermol.AbstractIntegrationTest;
//import com.example.Excermol.enums.UserRole;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.*;
//
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//public class OrganizationControllerIntegrationTest extends AbstractIntegrationTest {
//
//    private String adminCookie;
//    private String userCookie;
//
//
//    @BeforeEach
//    void setUp() {
//        createTestUser("admin-org-test@example.com", "password123", UserRole.ADMIN);
//        createTestUser("user-org-test@example.com", "password123", UserRole.USER);
//
//        adminCookie = loginAndGetAccessTokenCookie("admin-org-test@example.com", "password123");
//        userCookie = loginAndGetAccessTokenCookie("user-org-test@example.com", "password123");
//    }
//
//    @AfterEach
//    void tearDown() {
//        cleanupTestUser("admin-org-test@example.com");
//        cleanupTestUser("user-org-test@example.com");
//    }
//
////    USER-in Organization yarada bildiyini yoxlayır
//    @Test
//    void createOrganization_withUserRole_shouldSucceed() {
//        Map<String, Object> requestBody = Map.of(
//                "name", "Test Organization",
//                "domain", "testorg.com"
//        );
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add(HttpHeaders.COOKIE, userCookie);
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity("/api/organizations", request, String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(response.getBody()).contains("Test Organization");
//    }
//
////    token olmadan 401 alındığını yoxlayır
//    @Test
//    void createOrganization_withoutToken_shouldReturn401() {
//        Map<String, Object> requestBody = Map.of(
//                "name", "Test Organization",
//                "domain", "testorg2.com"
//        );
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity("/api/organizations", request, String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
//    }
//
//    @Test
//    void getAllOrganizations_withValidToken_shouldSucceed() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.COOKIE, userCookie);
//        HttpEntity<Void> request = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                "/api/organizations", HttpMethod.GET, request, String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
////    USER-in silə bilmədiyini yoxlayır (sənin hasAnyRole('ADMIN', 'MANAGER')
//    @Test
//    void deleteOrganization_withUserRole_shouldReturn403() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.COOKIE, userCookie);
//        HttpEntity<Void> request = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                "/api/organizations/1", HttpMethod.DELETE, request, String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
//    }
//
//
//}
