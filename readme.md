# Kenac Backend Services

This repository contains the backend microservices for **Microbank: A Full-Stack Microservice Banking Platform**. It includes the following modules:

* **config-service**: Spring Cloud Config Server for centralized configuration
* **discovery-server**: Eureka Server for service registration and discovery
* **auth-service**: Handles user registration, login, JWT issuance, and refresh
* **client-service**: Manages client profiles and KYC workflows
* **banking-service**: Processes deposits, withdrawals, and balance tracking
* **gateway-service**: Spring Cloud Gateway for routing, JWT validation, and rate limiting

---

## 🚀 Prerequisites

* **Java 17+**
* **Maven 3.6+**
* **Git**
* (Optional) **Docker & Docker Compose** for containerized setup

---

## 🔧 Project Structure

```
microbank-backend/
├── config-service/
├── discovery-server/
├── auth-service/
├── client-service/
├── banking-service/
├── gateway-service/
└── docker-compose.yml  (optional)
```

Each service is a standalone Spring Boot application with its own `pom.xml`, `src/main/java`, and `src/main/resources/application.yml`. All services use Spring Cloud for config, service discovery, and gateway patterns.

---

## ⚙️ Configuration

1. **Config Server** (`config-service`):

    * Hosts externalized config in the Git-backed config repository (by default itself).
    * Default port: `8888`
    * Verify `application.yml` for repository location.

2. **Eureka Discovery** (`discovery-server`):

    * Registers all microservices for load-balanced calls.
    * Default port: `8761`

3. **Service Names & Ports**:

   | Service          | spring.application.name | Default Port |
      | ---------------- | ----------------------- | ------------ |
   | config-service   | config-service          | 8888         |
   | discovery-server | discovery-server        | 8761         |
   | auth-service     | auth-service            | 5090         |
   | client-service   | client-service          | 5091         |
   | banking-service  | banking-service         | 5092         |
   | gateway-service  | gateway-service         | 5980         |

> **Tip:** Ports can be overridden in each service's `application.yml` or via `-Dserver.port=`.

---

## 🏃 Running Locally (Maven)

1. **Clone Repository**

   ```bash
   git clone https://github.com/your-org/microbank-backend.git
   cd microbank-backend
   ```

2. **Start Config Server**

   ```bash
   cd config-service
   mvn clean spring-boot:run
   ```

3. **Start Discovery Server**

   ```bash
   cd ../discovery-server
   mvn clean spring-boot:run
   ```

4. **Run Remaining Services**
   Launch each service in its own terminal:

   ```bash
   # Auth Service\   
   cd ../auth-service
   mvn clean spring-boot:run

   # Client Service\   
   cd ../client-service
   mvn spring-boot:run

   # Banking Service\   
   cd ../banking-service
   mvn spring-boot:run

   # Gateway Service\   
   cd ../gateway-service
   mvn spring-boot:run
   ```

5. **Verify Services**

    * Config:  `http://localhost:8888/actuator`
    * Eureka:  `http://localhost:8761`
    * Gateway: `http://localhost:5980/actuator/routes`

---

## 🐳 Docker Compose (Optional)

A `docker-compose.yml` is provided for containerized development. It spins up all services plus a PostgreSQL database.

```bash
# Build images
docker-compose build

# Start all containers
docker-compose up -d

# View logs
docker-compose logs -f
```

> **Note:** Ensure you update environment variables in `docker-compose.yml` for DB credentials and service URLs.

---

## 🔐 Security & Auth Flow

* **JWT Issuance**: `POST http://localhost:5090/auth/login` returns `accessToken` & `refreshToken`
* **Token Validation**: Gateway and each service validate JWT signature & expiry on incoming requests.
* **Blacklist Enforcement**: Auth service introspects blacklist status; tokens for blacklisted users are rejected.

---

Or use the included OpenAPI YAML located under `src/main/resources/api-docs` in each module.

---
