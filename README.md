# Spring Boot CRUD API with JWT Authentication & Error Logging

This is a Spring Boot backend application with the following features:

- REST API for managing users
- JWT-based authentication
- Role-based access control
- Global exception handling
- Logs error events to database
- Scheduled email reports (CSV/HTML)
- Environment-specific profiles using `.env`
- OpenAPI documentation via SpringDoc

## 🛠️ Tech Stack

- Java 17+
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Data JPA + Hibernate
- MySQL (or any SQL DB via JDBC)
- Spring Mail (for sending logs)
- SpringDoc OpenAPI
- SLF4J Logging

## 🔧 Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo.git
   cd backend
