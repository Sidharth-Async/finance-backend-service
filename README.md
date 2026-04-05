
# 🏦 Finance Data Processor API

A production-ready, secure RESTful backend service designed to manage personal financial transactions. This API demonstrates enterprise-grade architecture, featuring stateless JWT authentication, Role-Based Access Control (RBAC), robust data validation, and dynamic database querying.

## 🚀 Key Features

* **Stateless Authentication:** Secure user registration and login utilizing JSON Web Tokens (JWT) and BCrypt password encryption.
* **Role-Based Access Control (RBAC):** Hierarchical permissions system enforcing strict access rules for `VIEWER` and `ADMIN` roles.
* **User Data Isolation:** Database queries are automatically filtered by the authenticated JWT principal. Users can strictly only access their own financial records.
* **Financial Dashboard Aggregation:** Calculates and returns real-time mathematical summaries (Total Income, Total Expenses, Net Balance) using Java Streams.
* **Dynamic Record Filtering:** Utilizes custom JPQL queries to allow users to filter their transaction history dynamically via URL query parameters.
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
├── dto/          # Data Transfer Objects (LoginRequest, DashboardSummary)
├── exception/    # GlobalExceptionHandler
├── model/        # JPA Entities (User, Role, Transaction)
├── repository/   # Spring Data JPA Interfaces & Custom JPQL
├── security/     # JWT Utilities & Filters
└── service/      # Core Business Logic & Data Aggregation
```

## ⚙️ Getting Started

### Prerequisites
* Java Development Kit (JDK) 17 or higher
* Maven installed

### Run the Application (H2 Database Mode)
By default, the application runs on a lightweight, in-memory H2 database. 

```bash
git clone https://github.com/Sidharth-Async/finance-backend-service
cd finance-data-processor
mvn spring-boot:run
```
*(Note: The system automatically seeds an Admin user `admin` / `admin123` upon startup).*

## 📡 API Reference

### 🔓 Authentication (Public)
* **Register:** `POST /api/auth/register` 
  * Registers a new user with the default `VIEWER` role.
* **Login:** `POST /api/auth/login`
  * Authenticates user and returns the JWT Bearer token.

### 🛡️ Transactions (Requires JWT)
*All requests below must include the JWT in the `Authorization` header as a `Bearer` token.*

* **Create Transaction:** `POST /api/transactions`
  * Creates a new record tied to the logged-in user.
* **Get My Transactions:** `GET /api/transactions`
  * Retrieves all transactions belonging to the logged-in user.
  * *Optional Filters:* Append `?type=INCOME` or `?category=Salary` to filter results.
* **Get Dashboard Summary:** `GET /api/transactions/summary`
  * Returns calculated totals (Income, Expense, Net Balance) for the logged-in user.
* **Get Single Transaction:** `GET /api/transactions/{id}`
  * Retrieves a specific transaction (if owned by the user).
* **Delete Transaction:** `DELETE /api/transactions/{id}`
  * Deletes a specific transaction.

### 👑 Admin Dashboard (Requires ADMIN Role)
* **Get All Data:** `GET /api/transactions/all`
  * Allows admins to view every transaction across all users in the system.

## 🧪 Testing Guide (Postman)

**1. Register a User**
* **Method:** `POST`
* **URL:** `http://localhost:8080/api/auth/register`
* **Body (Raw JSON):**
```json
{
    "username": "batman",
    "password": "supersecretpassword"
}
```

**2. Login to get JWT**
* **Method:** `POST`
* **URL:** `http://localhost:8080/api/auth/login`
* **Body (Raw JSON):** Same as above.
* **Result:** Copy the `"token"` string from the response.

**3. Create a Transaction**
* **Method:** `POST`
* **URL:** `http://localhost:8080/api/transactions`
* **Auth:** Select `Bearer Token` and paste your JWT.
* **Body (Raw JSON):**
```json
{
    "amount": 5000.00,
    "type": "INCOME",
    "category": "Salary",
    "date": "2026-04-05",
    "description": "Monthly Salary"
}
```

**4. View Dashboard Summary**
* **Method:** `GET`
* **URL:** `http://localhost:8080/api/transactions/summary`
* **Auth:** Use your Bearer Token.
* **Result:** Returns calculated totals of your injected data.

**5. Test Dynamic Filters**
* **Method:** `GET`
* **URL:** `http://localhost:8080/api/transactions?type=INCOME`
* **Auth:** Use your Bearer Token.
* **Result:** Only returns transactions where the type strictly matches "INCOME".
