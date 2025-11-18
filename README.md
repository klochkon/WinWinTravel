# 🌍 WinWinTravel Microservice Ecosystem

Welcome to the **WinWinTravel** project. This solution implements a secure, distributed microservice architecture designed to handle user authentication and data processing tasks. The system is fully containerized and orchestrated using Docker Compose.

---

## 🏗️ System Architecture

The application is built upon **Java 21** and **Spring Boot 3.2.5**, utilizing a microservices pattern to separate concerns:

1.  **Gateway & Auth (Service A - `auth-api`):**
    * The entry point for all external requests (Port `8080`).
    * Handles **JWT Authentication** and **BCrypt** password hashing.
    * Manages the PostgreSQL database for persistent storage.
    * Acts as a secure client to the internal Data Service.

2.  **Data Transformation (Service B - `data-api`):**
    * An internal, stateless utility service (Port `8081`).
    * Performs text transformation logic (e.g., Uppercase conversion).
    * Secured via an internal `X-Internal-Token` header, inaccessible to the public internet.

3.  **Persistence (PostgreSQL):**
    * Stores user credentials and an audit log of all processing requests.

---

## ⚡️ Quick Start Guide

Follow these steps to launch the entire ecosystem on your local machine.

### Prerequisites
* **Docker Desktop** (running)
* **Maven**

### 1. Environmental variables

```properties
# --- Database Settings ---
DB_USER=postgres
DB_PASSWORD=secret_password
DB_URL=jdbc:postgresql://postgres-db:5432/WinWinTravel

# --- Security Secrets ---
# (Use a strong Base64 string for JWT signing)
SECURITY_JWT_SECRET_KEY=your_jwt_secret_token

# (Shared secret for inter-service communication)
INTERNAL_TOKEN=internal_super_secret_key_12345
```

### 2. Build the Services
Before creating Docker images, compile the source code into executable JARs:
```bash
mvn -f auth-api/pom.xml clean package -DskipTests
mvn -f data-api/pom.xml clean package -DskipTests
```

### 2. Launch via Docker Compose
Start the database and both microservices in a unified network:
```bash
docker compose up -d --build
```
Check docker ps to ensure auth-api, data-api, and postgres-db are up and running.


## Auth API (Service A) Endpoints

### 1. Public Endpoints (No Auth)

#### `POST /api/auth/register`
Registers a new user in the system.
* **Request:**
    ```json
    {
      "email": "user@example.com",
      "password": "password123"
    }
    ```
* **Response:** `HTTP 201 Created`

#### `POST /api/auth/login`
Authenticates a user and returns a JWT token.
* **Request:**
    ```json
    {
      "email": "user@example.com",
      "password": "password123"
    }
    ```
* **Response:**
    ```json
    { "token": "eyJhbGciOiJIUzI1NiJ9..." }
    ```

### 2. Protected Endpoints (Requires JWT)

#### `POST /api/process`
Processes text by calling Service B and logging the activity.
* **Auth:** Requires header `Authorization: Bearer <your_jwt_token>`
* **Request:**
    ```json
    { "text": "hello world" }
    ```
* **Response:**
    ```json
    { "result": "HELLO WORLD" }
    ```

## Data API (Service B)

### `POST /api/transform`

* **Auth:** Requires header `X-Internal-Token`.
* **Request:**
    ```json
    { "text": "some text" }
    ```
* **Response:**
    ```json
    { "result": "SOME TEXT" }
    ```


## 📞 Contact

For any questions regarding the application logic or architecture, please contact:

**+380684290064**
