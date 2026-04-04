
# 🏦 Finance Data Processor API

A production-ready, secure RESTful backend service designed to manage personal financial transactions. This API demonstrates enterprise-grade architecture, featuring stateless JWT authentication, Role-Based Access Control (RBAC), robust data validation, and global exception handling.

## 🚀 Key Features

* **Stateless Authentication:** Secure user registration and login utilizing JSON Web Tokens (JWT) and BCrypt password encryption.
* **Role-Based Access Control (RBAC):** Hierarchical permissions system enforcing strict access rules for `VIEWER` and `ADMIN` roles.
* **User Data Isolation:** Database queries are automatically filtered by the authenticated JWT principal. Users can strictly only access their own financial records.
* **Bulletproof Validation:** Jakarta Validation intercepts bad data before it reaches the service layer.
* **Global Exception Handling:** A custom `@RestControllerAdvice` intercepts application errors and returns beautifully formatted, predictable JSON responses.

## 🛠️ Technology Stack

* **Language:** Java 17+
* **Framework:** Spring Boot 4.x
* **Security:** Spring Security 6, JSON Web Tokens (jjwt)
* **Data Access:** Spring Data JPA, Hibernate
* **Database:** H2 In-Memory Database (Default) / PostgreSQL (Ready)
* **Build Tool:** Maven

## 📂 Project Architecture

```text
src/main/java/com/finance/data_processor/
├── config/       # Database Seeders
├── controller/   # REST Endpoints
├── dto/          # Data Transfer Objects
├── exception/    # GlobalExceptionHandler
├── model/        # JPA Entities (User, Role, Transaction)
├── repository/   # Spring Data JPA Interfaces
├── security/     # JWT Utilities & Filters
└── service/      # Core Business Logic
## ⚙️ Getting Started

### Prerequisites
* Java Development Kit (JDK) 17 or higher
* Maven installed

### 1. Clone & Run (H2 Database Mode)
By default, the application runs on a lightweight, in-memory H2 database. 

```bash
git clone https://github.com/Sidharth-Async/finance-backend-service.git
cd finance-data-processor
mvn spring-boot:run
```
*Note: The system automatically seeds an Admin user (`superadmin` / `admin123`) and the required roles upon startup.*

### 2. Upgrading to PostgreSQL (Optional)
To switch to a persistent database, update your `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

## 📡 API Reference

### Authentication (Public)
| Method | Endpoint | Description | Payload |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user (`VIEWER` role) | `{ "username": "...", "password": "..." }` |
| `POST` | `/api/auth/login` | Authenticate and receive a JWT | `{ "username": "...", "password": "..." }` |

### Transactions (Requires JWT)
*All requests must include the JWT in the `Authorization` header as a `Bearer` token.*

| Method | Endpoint | Description | Access Level |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/transactions` | Create a new transaction | Authenticated Users |
| `GET` | `/api/transactions` | Get transactions for the logged-in user | Authenticated Users |
| `GET` | `/api/transactions/{id}`| Get a specific transaction by ID | Authenticated Users |
| `DELETE`| `/api/transactions/{id}`| Delete a specific transaction | Authenticated Users |

### Admin Dashboard (Requires ADMIN Role)
| Method | Endpoint | Description | Access Level |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/transactions/all` | View every transaction across all users | `ADMIN` Only |

## 🧪 Testing Guide (Postman)

### Step 1: Register a User
* **URL:** `POST http://localhost:8080/api/auth/register`
* **Body (Raw JSON):**
  ```json
  {
      "username": "batman",
      "password": "supersecretpassword"
  }
  ```

### Step 2: Login to get JWT
* **URL:** `POST http://localhost:8080/api/auth/login`
* **Body (Raw JSON):** Same as above.
* **Result:** Copy the `"token"` string from the response.

### Step 3: Create a Transaction
* **URL:** `POST http://localhost:8080/api/transactions`
* **Auth:** Select `Bearer Token` and paste your JWT.
* **Body (Raw JSON):**
  ```json
  {
      "amount": 125.50,
      "type": "EXPENSE",
      "category": "Groceries",
      "date": "2026-04-04",
      "description": "Weekly grocery run"
  }
  ```

### Step 4: Test Admin Privileges (403 Forbidden)
* **URL:** `GET http://localhost:8080/api/transactions/all`
* **Auth:** Keep the `batman` token.
* **Result:** You will receive a `403 Forbidden` because standard users cannot access the global ledger. Login as `superadmin` to access this endpoint.
