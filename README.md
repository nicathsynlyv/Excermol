# 🚀 Excermol CRM

Excermol is a production-grade **Customer Relationship Management (CRM)** backend built with **Spring Boot**, designed to manage organizations, companies, contacts, sales pipelines, campaigns, tasks, and team collaboration — with enterprise-level security, testing, and deployment practices.

The project demonstrates a complete, real-world backend engineering workflow: from layered architecture and validation, through JWT/OAuth2 security and refresh-token session management, to Testcontainers-based integration testing and Docker containerization.

---

## ✨ Features

### 🔐 Authentication & Authorization

* **JWT-based authentication** with short-lived access tokens (15 min) and long-lived refresh tokens (30 days)
* **HttpOnly, SameSite=Strict cookies** for token delivery — protects against XSS and CSRF
* **OAuth2 Social Login** — Google and Facebook, with automatic account creation and email-based account linking
* **Role-Based Access Control (RBAC)** — `ADMIN`, `MANAGER`, `USER`, `VIEWER`, enforced via `@PreAuthorize` at the endpoint level and centrally in `SecurityConfig`
* **Refresh token rotation & revocation** — supports multi-device sessions, single-device logout, and "logout from all devices"
* **Forgot Password / OTP flow** — 4-digit OTP via email (Gmail SMTP), time-limited (5 min), single-use verification tokens
* **BCrypt password hashing**
* Custom `401`/`403` JSON error handlers (`JwtAuthenticationEntryPoint`, `CustomAccessDeniedHandler`) — consistent error contracts instead of framework defaults

### 🏢 Core CRM Modules

* Organizations, Companies, Persons (Contacts), Pipelines (Kanban-style deal tracking)
* Campaigns & Campaign Leads
* Tasks (Kanban board), Comments, Tags, Attachments
* Forms (custom form builder: fields, routing rules, public submissions, response tracking)
* Workspaces & Workspace Members (multi-tenant team structure)
* Settings: Notification preferences, Company custom attributes, third-party Integrations

### 🛡️ API Quality

* **Bean Validation** on every request DTO (`@NotBlank`, `@Email`, `@Size`, `@Min/@Max`, custom domain-format regex, etc.)
* **Centralized exception handling** (`@RestControllerAdvice`) — every domain exception maps to a consistent JSON error shape with the correct HTTP status (400/401/403/404/409/500)
* DTO-based request/response contracts (entities are never exposed directly)
* Swagger / OpenAPI documentation for every controller

### ✅ Testing

* **428+ unit tests** (JUnit 5 + Mockito) across all service-layer classes, covering success paths, not-found cases, conflict/business-rule violations, and edge cases
* **Integration tests** (Testcontainers + real PostgreSQL + `TestRestTemplate`) covering:
    * Full authentication lifecycle: login, register, refresh, logout, logout-all
    * Role-based access across core controllers (Organization, Company, Person, Pipeline, Task, Campaign, CampaignLead, Form, User)
    * Validation failures (400), not-found (404), conflicts (409), and unauthorized/forbidden access (401/403)
* Tests run against a real, isolated PostgreSQL instance spun up automatically per test run — no shared state, no mocking of the persistence layer

### 🐳 Deployment

* **Multi-stage Dockerfile** (Maven build stage → lightweight JRE runtime stage)
* **Docker Compose** — application + PostgreSQL, wired together with health checks (`depends_on: condition: service_healthy`)
* **Spring Boot Actuator health endpoint** (`/actuator/health`), exposed without authentication and used as the container health check
* All secrets (JWT secret, DB credentials, OAuth2 client secrets, mail credentials) externalized via environment variables — nothing sensitive is hardcoded or committed

---

## 🏗️ Project Architecture

The application follows a strict layered architecture:

```text
Controller  →  Service Interface  →  ServiceImpl  →  Repository  →  Database
                        ↑
                   DTO ↔ Mapper (manual, no auto-mapping libraries)
```

Cross-cutting concerns are isolated into their own packages:

```text
security/
├── config/          → SecurityConfig, PasswordConfig
├── jwt/             → JwtUtil, JwtAuthenticationFilter, CookieUtil,
│                       RefreshTokenService, PasswordResetService,
│                       custom exceptions & 401/403 handlers
├── userdetails/     → UserPrincipal (UserDetails + OAuth2User adapter),
│                       CustomUserDetailsService
└── oauth2/          → CustomOAuth2UserService, OAuth2AuthenticationSuccessHandler,
                        provider-specific user-info parsers (Google, Facebook)

exception/           → GlobalExceptionHandler + per-domain custom exceptions
entity/dtos/         → Request/Response DTOs per module
mapper/               → Manual Entity ↔ DTO mappers
```

Every entity follows the same conventions: `@Getter/@Setter` (never `@Data`, to avoid circular `toString`/`equals` issues on bidirectional relations), `IDENTITY` primary keys, `@PrePersist/@PreUpdate` timestamps, and dedicated custom exceptions per domain (e.g. `CompanyNotFoundException`, `DomainAlreadyExistsException`).

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| Language / Runtime | Java 21, Spring Boot 3.5 |
| Web | Spring Web (MVC), Bean Validation |
| Security | Spring Security, JWT (JJWT 0.12.6), OAuth2 Client (Google, Facebook) |
| Persistence | Spring Data JPA, Hibernate, PostgreSQL |
| Email | Spring Mail (Gmail SMTP) — OTP delivery |
| Testing | JUnit 5, Mockito, Testcontainers (PostgreSQL), Spring Boot Test |
| Documentation | springdoc-openapi (Swagger UI) |
| Build | Maven |
| Containerization | Docker, Docker Compose |
| Utilities | Lombok |

---

## 📚 API Documentation

Once the application is running, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## ⚙️ Getting Started

### Prerequisites

* Docker Desktop (recommended — see [Running with Docker](#-running-with-docker)), **or**
* Java 21, Maven, and a local PostgreSQL instance if running without Docker

### 1. Clone the repository

```bash
git clone https://github.com/your-username/excermol.git
cd excermol
```

### 2. Configure environment variables

Copy the example file and fill in your own values:

```bash
cp .env.example .env
```

Required variables (see `.env.example` for the full list):

| Variable | Description |
|---|---|
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | PostgreSQL connection |
| `JWT_SECRET`, `JWT_EXPIRATION`, `JWT_REFRESH_EXPIRATION` | JWT signing key & token lifetimes |
| `GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET` | Google OAuth2 credentials |
| `FACEBOOK_CLIENT_ID` / `FACEBOOK_CLIENT_SECRET` | Facebook OAuth2 credentials |
| `MAIL_USERNAME` / `MAIL_PASSWORD` | Gmail SMTP credentials (used for OTP emails) |
| `OAUTH2_REDIRECT_URI` | Frontend URL to redirect to after a successful social login |

> `.env` is git-ignored — never commit real secrets.

### 3a. Running with Docker (recommended)

```bash
docker-compose up --build
```

This starts PostgreSQL and the application together, wired via Docker's internal network, with health checks ensuring the app only starts once the database is ready.

The API will be available at `http://localhost:8080`.

Check health:

```bash
curl http://localhost:8080/actuator/health
```

Stop everything:

```bash
docker-compose down
```

Stop and wipe the database volume:

```bash
docker-compose down -v
```

### 3b. Running locally (without Docker)

Make sure PostgreSQL is running and reachable at the URL in your `.env`, then:

```bash
mvn spring-boot:run  for local
docker-compose up for docker

```

---

## 🔑 Authentication Flow

```text
POST /auth/register          → create account, sets accessToken + refreshToken cookies
POST /auth/login              → authenticate, sets accessToken + refreshToken cookies
POST /auth/refresh             → issues a new accessToken using the refreshToken cookie
POST /auth/logout              → revokes the current refresh token, clears cookies
POST /auth/logout-all          → revokes all refresh tokens for the current user
POST /auth/forgot-password      → sends a 4-digit OTP to the user's email
POST /auth/verify-otp            → validates the OTP
POST /auth/reset-password         → sets a new password (requires a verified OTP)

GET /oauth2/authorization/google   → starts Google login
GET /oauth2/authorization/facebook  → starts Facebook login
```

Access tokens are short-lived (15 minutes) by design; clients are expected to call `/auth/refresh` transparently when a request returns `401`.

---

## 🧪 Running Tests

```bash
# Unit tests only
mvn test -Dtest=*ServiceImplTest

# Full test suite, including Testcontainers-based integration tests
mvn test
```

Integration tests require Docker to be running locally (Testcontainers spins up a disposable PostgreSQL container per test session).

---

## 📖 Engineering Practices

* SOLID principles, dependency injection via constructor, consistent layered architecture across all 25+ modules
* Defense-in-depth authorization: URL-level rules in `SecurityConfig` + method-level `@PreAuthorize` + business-rule guards in the service layer (e.g. system attributes cannot be modified regardless of role)
* Fail-safe validation: Bean Validation at the DTO boundary, domain-uniqueness checks in the service layer, and a global exception translator so no unhandled exception ever leaks a raw stack trace to a client
* Secrets never hardcoded — externalized via environment variables from day one of the Docker phase

---

## 🔮 Future Improvements

* Ownership-based authorization (e.g. restrict workspace/notification actions to the resource's actual owner, not just role)
* Rate limiting on authentication endpoints
* Scheduled cleanup of expired refresh tokens and password-reset tokens
* CI/CD pipeline (GitHub Actions)
* Redis-backed caching for frequently-read, rarely-changed data
* Centralized structured logging / monitoring

---

## 👨‍💻 Author

**Nijat Huseynaliyev**
Backend Developer | Java & Spring Boot