package com.berkdagli.sowa.service;

import com.berkdagli.sowa.model.User;
import com.berkdagli.sowa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "test@example.com", "password", "USER");
    }

    @Test
    void createUser_Success() {
        // Arrange
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L); // Simulate DB ID generation
            return user;
        });

        // Act
        User createdUser = userService.createUser("testuser", "test@example.com", "password");

        // Assert
        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals("USER", createdUser.getRole());

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_EmailTaken() {
        // Arrange
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(testUser));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser("newuser", "test@example.com", "password");
        });

        assertEquals("Email already taken", exception.getMessage());

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder, never()).encode(any(String.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticate_Success() {
        // Arrange
        testUser.setPassword("encodedPassword"); // set encoded password on user found in DB
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

        // Act
        User authenticatedUser = userService.authenticate("test@example.com", "rawPassword");

        // Assert
        assertNotNull(authenticatedUser);
        assertEquals("testuser", authenticatedUser.getUsername());

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("rawPassword", "encodedPassword");
    }

    @Test
    void authenticate_InvalidCredentials() {
        // Arrange
        testUser.setPassword("encodedPassword");
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.authenticate("test@example.com", "wrongPassword");
        });

        assertEquals("Invalid credentials", exception.getMessage());

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
    }

    @Test
    void authenticate_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.authenticate("nonexistent@example.com", "password");
        });

        assertEquals("Invalid credentials", exception.getMessage());

        verify(userRepository).findByEmail("nonexistent@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void loadUserByUsername_Success() {
        // Arrange
        testUser.setPassword("encodedPassword"); // matches UserDetails expect
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        org.springframework.security.core.userdetails.UserDetails userDetails = userService
                .loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistent@example.com");
        });

        verify(userRepository).findByEmail("nonexistent@example.com");
    }
}
