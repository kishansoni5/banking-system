# Banking System — REST API Backend

A secure and scalable RESTful backend application simulating core banking operations. Built to demonstrate proficiency in enterprise Java development using the Spring Ecosystem.

## 🛠️ Tech Stack & Key Concepts
* **Spring Boot 3 & Spring Core:** Dependency Injection, Inversion of Control (IoC), and auto-configuration.
* **Spring MVC:** Building RESTful web services using `@RestController`, `@RequestMapping`, and handling HTTP status codes.
* **Maven:** Dependency and build lifecycle management.
* **Data Validation:** (Optional, if you used it) Input validation using Hibernate Validator / Jakarta Validation annotations.

## 🚀 Features
* **Account Management:** Create accounts, fetch account details, and check balances.
* **Transactions:** Secure deposit, withdrawal, and fund transfer API endpoints.
* **Error Handling:** Centralized exception handling using `@ControllerAdvice` for clean API responses.

## 📋 API Endpoints Preview
* `POST /api/accounts` - Create a new bank account.
* `GET /api/accounts/{id}` - Retrieve details of a specific account.
* `POST /api/accounts/{id}/deposit` - Deposit money.
* `POST /api/accounts/{id}/withdraw` - Withdraw money.

## 🏃 How to Run Locally
1. Clone the repository: `git clone <repo-url>`
2. Open it in Eclipse as an **Existing Maven Project**.
3. Update `src/main/resources/application.properties` with your local database configurations.
4. Run the application as a **Java Application** via the main Spring Boot class.