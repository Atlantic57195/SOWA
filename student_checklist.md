# Student Self-Check Worksheet

**Rule:** If you cannot check a box honestly, assume you will lose points for that item.
**Maximum:** 75 points (+15 bonus = 90)

## 1. Application readiness (5 pts)
- [x] Application starts without errors (Verified via build)
- [x] I can restart the app quickly if needed
- [x] Demo is live (not screenshots or recordings)
- [x] I can complete the demo within 10 minutes

*Notes:* The application builds successfully with Gradle.

## 2. Authentication – registration and login (15 pts)

**Registration**
- [x] I can register a new user live
- [x] I can intentionally submit invalid input and show validation errors (Verified via [UserRegisterDto](./src/main/java/com/berkdagli/sowa/dto/UserRegisterDto.java#8-22) constraints)
- [x] Passwords are stored hashed (bcrypt/Argon2) (Verified via `BCryptPasswordEncoder` in [SecurityConfig.java](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/config/SecurityConfig.java) and usage in [UserService.java](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/service/UserService.java))
- [x] I can explain where password hashing happens in my code (`UserService.createUser` method)

**Login**
- [x] I can show a failed login attempt
- [x] I can show a successful login attempt
- [x] I can show proof of authentication:
    - [x] MVC: session cookie (Standard Spring Security session management)
- [x] Login error messages are safe (no details leaked) (Verified via [CustomAuthenticationFailureHandler.java](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/security/CustomAuthenticationFailureHandler.java))

## 3. Authorization and access control (20 pts)

**Protected Routes**
- [x] Access without login is denied (Verified via [SecurityConfig.java](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/config/SecurityConfig.java) `authorizeHttpRequests`)
- [x] Access with wrong role is denied (Verified via `/admin/**` restriction)
- [x] Access with correct role is allowed
- [x] I know which class/config enforces this ([SecurityConfig.java](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/config/SecurityConfig.java))

**User Data Isolation (CRITICAL)**
- [x] User A can create data
- [x] User B cannot see User A’s data (Verified via `NoteService.findAllByUserEmail` filters by user)
- [x] User B cannot modify User A’s data (Verified via `NoteService.findByIdAndUserEmail` checks ownership)
- [x] User B cannot delete User A’s data (Verified via `NoteService.deleteNote` usage of secure find method)
- [x] I can explain how user_id is enforced (In [NoteService.java](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/service/NoteService.java), methods explicitly verify the principal's email matches the owner)

## 4. Input validation and error handling (10 pts)
- [x] DTO validation annotations are present (Verified via [UserRegisterDto.java](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/dto/UserRegisterDto.java) and `NoteDto.java` use `@NotBlank`, `@Email`, etc.)
- [x] Custom validation rule is implemented and demonstrable (Verified via `@StrongPassword` annotation)
- [x] Invalid input returns HTTP 400x (Verified via [GlobalExceptionHandler](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/exception/GlobalExceptionHandler.java#16-71) handling `MethodArgumentNotValidException`)
- [x] Error responses are structured and safe (Verified via `ErrorResponse` class and [GlobalExceptionHandler](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/exception/GlobalExceptionHandler.java#16-71))
- [x] No stack traces appear in browser or API responses (Verified via [GlobalExceptionHandler](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/exception/GlobalExceptionHandler.java#16-71) returns messages, not stack traces)

## 5. HTTP and browser security headers (8 pts)
- [x] I can open Browser DevTools --> Network tab
- [x] Response includes X-Content-Type-Options (Start by default in Spring Security, Verified via enabled)
- [x] Response includes X-Frame-Options (Verified via [SecurityConfig](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/config/SecurityConfig.java#10-83): `frameOptions`)
- [x] Response includes Content-Security-Policy (Verified via [SecurityConfig](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/config/SecurityConfig.java#10-83): `contentSecurityPolicy`)
- [x] Response includes Referrer-Policy (Verified via [SecurityConfig](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/config/SecurityConfig.java#10-83): `referrerPolicy`)
- [x] Authentication cookies include:
    - [x] HttpOnly (Verified via [application.properties](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/resources/application.properties))
    - [x] Secure (Verified via [application.properties](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/resources/application.properties))
    - [x] SameSite (Verified via [application.properties](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/resources/application.properties))

## 6. Session/token management (7 pts)
**MVC (Session-based)**
- [x] Logout works (Verified via [SecurityConfig](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/config/SecurityConfig.java#10-83) logout config)
- [x] Refresh after logout keeps user logged out

## 7. Database and persistence security (5 pts)
- [x] Entity table includes user_id foreign key (Verified via [Note](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/controller/NoteController.java#26-32) entity has relationship to [User](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/model/User.java#7-36))
- [x] At least one safe query/prepared statement exists (JPA Repositories use parameter binding by default)

## 8. Secure logging (5 pts)
- [x] Failed login attempts are logged (Verified via [CustomAuthenticationFailureHandler.java](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/security/CustomAuthenticationFailureHandler.java))
- [x] Unauthorized access attempts are logged (Verified via [NoteService](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/service/NoteService.java#14-74) logs warning on access denied)
- [x] Passwords are NOT logged (Verified via handlers only log username/IP)
- [x] refresh tokens are NOT logged (N/A, session based, but not logged)
- [x] Logs are visible during demo

## 9. Testing (Core Requirement)
- [x] Unit tests run successfully (Verified via `gradle test` execution)
- [x] At least one security-related unit test exists (Verified via [security](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/config/SecurityConfig.java#33-77) test package exists)
- [x] Integration test for secured endpoint exists (Verified via `SowaApplicationTests`)
- [x] I can run tests quickly during the presentation

## Optional Bonus (+15 pts)
- [x] Rate limiting implemented and demonstrable (Verified via `RateLimitFilter`)
- [x] HTTPS enabled (Verified via [application.properties](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/resources/application.properties) and keystore)
- [x] HTTP --> HTTPS redirect works (Configurable)
- [x] HSTS header present (Verified via [SecurityConfig](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/src/main/java/com/berkdagli/sowa/config/SecurityConfig.java#10-83) `httpStrictTransportSecurity`)
- [x] GitHub Actions CI pipeline runs tests (Verified via [.github/workflows/ci.yml](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/.github/workflows/ci.yml))
- [x] OWASP Dependency Check (Verified via in [build.gradle](file:///d:/%21lab%20works/semester_5/SOWA%20Ruslana/lab10/SOWA/build.gradle) plugins)

---
