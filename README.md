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

## Application Routes & API

### 1. Client Pages (Thymeleaf Views)
These endpoints return HTML pages rendered on the server.
- **Root/Login:** `GET /` - Redirects to login page or serves login page.
- **Login:** `GET /login` - Custom login page.
- **Register:** `GET /register` - Registration page.
- **Home/Hello:** `GET /hello` - Accessible to authenticated users.
- **Admin Dashboard:** `GET /admin` - Restricted to users with `ADMIN` role.
- **Access Denied:** `GET /access-denied` - Shown when a user tries to access unauthorized content.

### 2. Note Operations
Standard CRUD operations for notes, handled via HTML forms and redirects.
- **List Notes:** `GET /notes` - Displays all notes for the current user.
- **New Note Form:** `GET /notes/new` - Shows the form to create a new note.
- **Create Note:** `POST /notes` - Handles the creation of a note.
- **Edit Note Form:** `GET /notes/edit/{id}` - Shows the form to edit an existing note.
- **Update Note:** `POST /notes/update/{id}` - Handles the update of a note.
- **Delete Note:** `POST /notes/delete/{id}` - Deletes a specific note.

### 3. REST API (User Management)
JSON endpoints for programmatic user management.

#### Register User
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
    ```json
    {
        "id": 1,
        "username": "testuser",
        "email": "test@example.com",
        "role": "USER"
    }
    ```
  - `400 Bad Request`: Validation error or Email already taken (JSON)

#### Login User
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
    ```json
    {
        "message": "Login successful",
        "username": "testuser"
    }
    ```
  - `400 Bad Request`: Invalid credentials (JSON)

## Project Structure
- `model`: JPA entities
- `repository`: Data access
- `service`: Business logic (Validation, Auth)
- `controller`: REST endpoints
- `config`: Security configuration
- `db/migration`: SQL migration scripts
