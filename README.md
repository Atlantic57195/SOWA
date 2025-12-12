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
- **GET** `/api/hello`
- Response: `Hello, user!`

### 2. Register User
- **POST** `/api/register`
- Body (JSON):
  ```json
  {
      "username": "testuser",
      "email": "test@example.com",
      "password": "secretpassword"
  }
  ```

### 3. Login User
- **POST** `/api/login`
- Body (JSON):
  ```json
  {
      "email": "test@example.com",
      "password": "secretpassword"
  }
  ```

## Project Structure
- `model`: JPA entities
- `repository`: Data access
- `service`: Business logic (Validation, Auth)
- `controller`: REST endpoints
- `config`: Security configuration
- `db/migration`: SQL migration scripts
