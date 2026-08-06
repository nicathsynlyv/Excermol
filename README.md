# 🚀 Excermol CRM
[![CI Pipeline](https://github.com/nicathsynlyv/excermol/actions/workflows/ci.yml/badge.svg)](https://github.com/nicathsynlyv/excermol/actions/workflows/ci.yml)

Excermol is a production-grade **Customer Relationship Management (CRM)** backend built with **Spring Boot**, covering organizations, companies, contacts, sales pipelines, campaigns, tasks, forms, and team collaboration — engineered with enterprise-level security, automated testing, containerization, and a full CI/CD pipeline deploying to a live server.

**🌐 Live:** [https://nicathsynlyv.com](https://nicathsynlyv.com)

---

## ✨ Features

### 🔐 Authentication & Authorization

* **JWT authentication** — short-lived access tokens (15 min) + long-lived refresh tokens (30 days), supporting multiple simultaneous device sessions
* **HttpOnly, Secure, SameSite=Strict cookies** for token delivery — protects against XSS and CSRF, with an environment-driven `Secure` flag (`false` locally, `true` in production over HTTPS)
* **OAuth2 Social Login** — Google and Facebook, with automatic account creation and email-based account linking for existing users
* **Role-Based Access Control (RBAC)** — `ADMIN`, `MANAGER`, `USER`, `VIEWER`, enforced both centrally in `SecurityConfig` (URL-level) and via `@PreAuthorize` (method-level) across every controller
* **Ownership-based authorization** — sensitive self-service actions (e.g. leaving a workspace) resolve the acting user from the JWT/session rather than trusting a client-supplied ID
* **Refresh token rotation & revocation** — single-device logout, "logout from all devices", and automatic invalidation on password reset
* **Forgot Password / OTP flow** — 4-digit OTP delivered via email (Gmail SMTP), 5-minute expiry, single-use, two-step verify-then-reset design
* **Rate limiting (Redis-backed)** — login attempts are throttled per email *and* per IP (5 attempts / 15-minute lockout), preventing brute-force attacks
* **Scheduled cleanup jobs** — expired refresh tokens and password-reset tokens are purged automatically on a daily schedule
* **BCrypt password hashing**
* Custom, consistent `401` / `403` / `429` JSON error responses (`JwtAuthenticationEntryPoint`, `CustomAccessDeniedHandler`, rate-limit handler) instead of framework defaults

### 🏢 Core CRM Modules

* Organizations, Companies, Persons (Contacts), Pipelines (deal tracking)
* Campaigns & Campaign Leads
* Tasks, Comments, Tags, Attachments
* Forms (custom form builder: fields, routing rules, response tracking)
* Workspaces & Workspace Members (team structure)
* Settings: notification preferences, company custom attributes, third-party integrations

### 🛡️ API Quality

* Bean Validation on every request DTO (`@NotBlank`, `@Email`, `@Size`, numeric bounds, domain-format regex, etc.)
* Centralized exception handling (`@RestControllerAdvice`) — every domain and security exception maps to a consistent JSON error shape with the correct HTTP status (400 / 401 / 403 / 404 / 409 / 429 / 500)
* DTO-based request/response contracts — entities are never exposed directly
* Swagger / OpenAPI documentation for every controller

### ✅ Testing

* **400+ unit tests** (JUnit 5 + Mockito) across the service layer — success paths, not-found cases, conflict/business-rule violations, edge cases
* **Integration tests** (Testcontainers + real PostgreSQL + `TestRestTemplate`) covering the full authentication lifecycle (login, register, refresh, logout, logout-all) and role-based access across core controllers (Organization, Company, Person, Pipeline, Task, Campaign, CampaignLead, Form, User)
* Tests run against a disposable, isolated PostgreSQL instance spun up per test session — no mocking of the persistence layer, no shared state between runs

### 🐳 Containerization

* **Multi-stage Dockerfile** — Maven build stage → lightweight JRE runtime stage
* **Docker Compose** — application, PostgreSQL, and Redis wired together with health checks (`depends_on: condition: service_healthy`)
* **Spring Boot Actuator health endpoint** (`/actuator/health`), unauthenticated and used as the container health check
* All secrets externalized via environment variables — nothing sensitive is hardcoded or committed

### ⚙️ CI/CD & Deployment

* **GitHub Actions pipeline**, triggered on every push/PR to `main`:
  1. **Test** — runs the full test suite (unit + Testcontainers integration tests) against ephemeral PostgreSQL and Redis service containers
  2. **Build & Push** — builds the Docker image and pushes it to Docker Hub (`latest` + commit-SHA tags), only after tests pass
  3. **Deploy** — SSHes into the production server (dedicated deploy key), pulls the freshly built image, and restarts the container — a fully automated, zero-manual-step deployment
* **Live infrastructure**: Ubuntu 24.04 VPS (Contabo), custom domain (Spaceship, DNS A-record), **Nginx** reverse proxy, and a **Let's Encrypt (Certbot)** SSL certificate — the API is served over HTTPS with automatic certificate renewal
* SSH access to the server is key-only (password authentication disabled), with `ufw` restricting inbound traffic to SSH, HTTP, and HTTPS

---

## 🏗️ Project Architecture

```text
Controller  →  Service Interface  →  ServiceImpl  →  Repository  →  Database
                        ↑
                   DTO ↔ Mapper (manual, no auto-mapping libraries)
```

Cross-cutting concerns are isolated into dedicated packages:

```text
security/
├── config/          → SecurityConfig, PasswordConfig
├── jwt/             → JwtUtil, JwtAuthenticationFilter, CookieUtil,
│                       RefreshTokenService, PasswordResetService,
│                       RateLimitingService, TokenCleanupService,
│                       custom exceptions & 401/403/429 handlers
├── userdetails/     → UserPrincipal (UserDetails + OAuth2User adapter),
│                       CustomUserDetailsService
└── oauth2/          → CustomOAuth2UserService, OAuth2AuthenticationSuccessHandler,
                        provider-specific user-info parsers (Google, Facebook)

exception/           → GlobalExceptionHandler + per-domain custom exceptions
entity/dtos/         → Request/Response DTOs per module
mapper/               → Manual Entity ↔ DTO mappers
```

Every entity follows the same conventions: `@Getter/@Setter` (never `@Data`, to avoid issues with bidirectional JPA relations), `IDENTITY` primary keys, `@PrePersist`/`@PreUpdate` timestamps, and dedicated custom exceptions per domain.

Authorization follows a **defense-in-depth** approach: URL-level rules in `SecurityConfig`, method-level `@PreAuthorize` per endpoint, and business-rule guards inside the service layer (e.g. system-level attributes cannot be modified regardless of role; a user can only revoke their own sessions).

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| Language / Runtime | Java 21, Spring Boot 3.5 |
| Web | Spring Web (MVC), Bean Validation |
| Security | Spring Security, JWT (JJWT 0.12.6), OAuth2 Client (Google, Facebook) |
| Persistence | Spring Data JPA, Hibernate, PostgreSQL |
| Caching / Rate Limiting | Redis (Spring Data Redis) |
| Email | Spring Mail (Gmail SMTP) — OTP delivery |
| Testing | JUnit 5, Mockito, Testcontainers (PostgreSQL) |
| Documentation | springdoc-openapi (Swagger UI) |
| Build | Maven |
| Containerization | Docker, Docker Compose |
| CI/CD | GitHub Actions, Docker Hub |
| Infrastructure | Contabo VPS (Ubuntu 24.04), Nginx, Let's Encrypt / Certbot, Spaceship DNS |
| Utilities | Lombok |

---

## 📚 API Documentation

```text
https://nicathsynlyv.com/swagger-ui/index.html
```

(or `http://localhost:8080/swagger-ui/index.html` when running locally)

---

## ⚙️ Getting Started (Local Development)

### Prerequisites

* Docker Desktop (recommended), **or** Java 21 + Maven + local PostgreSQL/Redis

### 1. Clone the repository

```bash
git clone https://github.com/your-username/excermol.git
cd excermol
```

### 2. Configure environment variables

```bash
cp .env.example .env
```

Fill in your own values. Key variables:

| Variable | Description |
|---|---|
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | PostgreSQL connection |
| `REDIS_HOST`, `REDIS_PORT` | Redis connection (for rate limiting) |
| `JWT_SECRET`, `JWT_EXPIRATION`, `JWT_REFRESH_EXPIRATION` | JWT signing key & token lifetimes |
| `GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET` | Google OAuth2 credentials |
| `FACEBOOK_CLIENT_ID` / `FACEBOOK_CLIENT_SECRET` | Facebook OAuth2 credentials |
| `MAIL_USERNAME` / `MAIL_PASSWORD` | Gmail SMTP credentials (OTP emails) |
| `OAUTH2_REDIRECT_URI` | Where the client is redirected after a successful social login |
| `COOKIE_SECURE` | `false` locally, `true` in production (requires HTTPS) |

> `.env` is git-ignored — never commit real secrets.

### 3. Run with Docker Compose

```bash
docker-compose up --build
```

This starts the application, PostgreSQL, and Redis together, wired via Docker's internal network, with health checks ensuring the app only starts once its dependencies are ready.

```bash
curl http://localhost:8080/actuator/health
```

### 4. Run locally without Docker (alternative)

```bash
mvn spring-boot:run
```

---

## 🔑 Authentication Flow

```text
POST /auth/register             → create account, sets accessToken + refreshToken cookies
POST /auth/login                 → authenticate, sets accessToken + refreshToken cookies
POST /auth/refresh                → issues a new accessToken using the refreshToken cookie
POST /auth/logout                  → revokes the current refresh token, clears cookies
POST /auth/logout-all               → revokes all refresh tokens for the current user
POST /auth/forgot-password           → sends a 4-digit OTP to the user's email
POST /auth/verify-otp                 → validates the OTP
POST /auth/reset-password              → sets a new password (requires a verified, unexpired OTP)

GET /oauth2/authorization/google    → starts Google login
GET /oauth2/authorization/facebook   → starts Facebook login
```

Access tokens are intentionally short-lived; clients are expected to call `/auth/refresh` transparently whenever a request returns `401`. Login is rate-limited per email and per IP to mitigate brute-force attempts.

---

## 🧪 Running Tests

```bash
# Unit tests only
mvn test -Dtest=*ServiceImplTest

# Full suite, including Testcontainers-based integration tests (requires Docker)
mvn test
```

The same suite runs automatically in CI on every push, against ephemeral PostgreSQL and Redis containers.

---

## 🚢 CI/CD Pipeline

```text
push to main
    │
    ▼
[ test ]  →  full unit + integration suite (Postgres & Redis as service containers)
    │
    ▼
[ docker-build-and-push ]  →  multi-stage image built & pushed to Docker Hub
    │
    ▼
[ deploy ]  →  SSH into the production server, pull the new image, restart the container
```

Deployment credentials are scoped to a dedicated, key-only SSH deploy user with no other access to the developer's personal machine.

---

## 📖 Engineering Practices

* SOLID principles, constructor-based dependency injection, a consistent layered architecture across 25+ modules
* Defense-in-depth authorization at every layer (URL, method, and business-rule level)
* Fail-safe validation at the DTO boundary combined with domain-uniqueness checks in the service layer
* No unhandled exception ever leaks a raw stack trace to a client — everything is translated into a structured JSON error
* Secrets externalized from day one; the same `.env`-driven configuration works identically in local, CI, and production environments

---

## 🔮 Future Improvements

* Extend ownership-based authorization to remaining self-service endpoints (notification settings, workspace member management)
* Zero-downtime deployment strategy (blue-green or rolling restart)
* Centralized structured logging / monitoring (e.g. Grafana + Loki)
* Frontend application

---

## 👨‍💻 Author

**Nijat Huseynaliyev**
Backend Developer | Java & Spring Boot

