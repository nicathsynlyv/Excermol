# 🚀 Excermol

Excermol is a backend application developed with Spring Boot for managing users, activities, and exercise-related operations. The project demonstrates modern backend development practices including authentication, authorization, API documentation, validation, exception handling, and clean architecture principles.

---

## ✨ Features

### 🔐 Authentication & Authorization

* JWT-based Authentication
* Spring Security Integration
* Role-Based Access Control (RBAC)
* BCrypt Password Encryption

### 👤 User Management

* Create User
* Update User
* Delete User
* Get User Details

### 🏋️ Activity Management

* Create Activity
* Update Activity
* Delete Activity
* Retrieve Activities

### 🔗 User-Activity Management

* Assign activities to users
* Manage user participation records

### 🛡️ API Quality

* Request Validation
* Global Exception Handling
* DTO-Based Data Transfer
* RESTful API Design
* Swagger/OpenAPI Documentation

---

## 🏗️ Project Architecture

The application follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Additional layers:

```text
DTO
Security
Configuration
Validation
Exception Handling
```

---

## 🛠️ Technology Stack

### Backend

* Java 17
* Spring Boot
* Spring Web

### Security

* Spring Security
* JWT Authentication

### Database

* PostgreSQL
* Spring Data JPA
* Hibernate

### Documentation

* Swagger / OpenAPI

### Build Tools

* Maven
* Lombok

---

## 📚 API Documentation

Swagger UI is available after running the application:

```bash
http://localhost:8080/swagger-ui/index.html
```

---

## ⚙️ Getting Started

### Clone Repository

```bash
git clone https://github.com/your-username/excermol.git
```

### Configure Database

Update your `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/excermol
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### Run Application

```bash
mvn spring-boot:run
```

---

## 📖 Engineering Practices

* Object-Oriented Programming (OOP)
* SOLID Principles
* Dependency Injection
* Layered Architecture
* Separation of Concerns
* Clean Code Principles

---

## 🔮 Future Improvements

* Refresh Token Support
* Docker Containerization
* Unit Testing (JUnit & Mockito)
* Integration Testing
* Redis Caching
* CI/CD Pipeline
* Monitoring & Logging

---

security 
com.example.Excermol
├── security/
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   └── OAuth2Config.java (sonra)
│   ├── jwt/
│   │   ├── JwtUtil.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtAuthenticationEntryPoint.java
│   ├── userdetails/
│   │   ├── UserPrincipal.java
│   │   └── CustomUserDetailsService.java
│   └── oauth2/ (sonra)
│       ├── CustomOAuth2UserService.java
│       └── OAuth2AuthenticationSuccessHandler.java
└── controller/
└── AuthController.java

## 👨‍💻 Author

Nijat Huseynaliyev

Backend Developer | Java & Spring Boot
