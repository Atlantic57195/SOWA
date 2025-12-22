# SOWA - Spring Boot Application

## Project Description
SOWA is a Spring Boot application featuring user authentication, database migration with Flyway, and SQLite persistence.

## Technologies
- **Java 17+**
- **Spring Boot 3+**
- **Spring Data JPA**
- **Spring Security**
- **Flyway** (Migration)
- **SQLite** (Database)
- **Lombok**

## Setup & Configuration

1. **Environment Variables**:
   Ensure you have a `.env` file in the project root (copied from `.env.example`).
   ```properties
   DB_FILE_NAME=sowa.db
   ```
   **Note**: The `.env` file should be kept secret and is ignored by git.

2. **Database**:
   The application uses SQLite. The database file (`sowa.db` by default) will be created automatically in the project root.

## Running the Application
Run the application using Gradle:
```bash
./gradlew bootRun
```
The server will start at `http://localhost:8080`.

## API Endpoints

### 1. Hello Check
- **Method:** `GET`
- **Path:** `/api/hello`
- **Response:** `200 OK` - Text: `Hello, user!`

### 2. Register User
- **Method:** `POST`
- **Path:** `/api/register`
- **Headers:**
  - `X-Source` (Optional): Source of request (default: `Web`)
- **Body (JSON):**
  ```json
  {
      "username": "testuser",
      "email": "test@example.com",
      "password": "StrongPassword123!" 
  }
  ```
- **Responses:**
  - `201 Created`: User object (JSON)
  - `400 Bad Request`: Validation error or Email already taken (JSON)

### 3. Login User
- **Method:** `POST`
- **Path:** `/api/login`
- **Body (JSON):**
  ```json
  {
      "email": "test@example.com",
      "password": "StrongPassword123!"
  }
  ```
- **Responses:**
  - `200 OK`: Success message (JSON)
  - `400 Bad Request`: Invalid credentials (JSON)

## Project Structure
- `model`: JPA entities
- `repository`: Data access
- `service`: Business logic (Validation, Auth)
- `controller`: REST endpoints
- `config`: Security configuration
- `db/migration`: SQL migration scripts
