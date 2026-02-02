package com.berkdagli.sowa.controller;

import com.berkdagli.sowa.dto.UserLoginDto;
import com.berkdagli.sowa.dto.LoginResponseDto;
import com.berkdagli.sowa.dto.UserRegisterDto;
import com.berkdagli.sowa.dto.UserResponseDto;
import com.berkdagli.sowa.model.User;
import com.berkdagli.sowa.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(
            @RequestHeader(value = "X-Source", required = false, defaultValue = "Web") String source,
            @Valid @RequestBody UserRegisterDto request) {

        System.out.println("Registration request from source: " + source);

        User user = userService.createUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword());
        return ResponseEntity.status(201).body(UserResponseDto.fromUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody UserLoginDto request) {
        User user = userService.authenticate(
                request.getEmail(),
                request.getPassword());
        return ResponseEntity.ok(new LoginResponseDto("Login successful", user.getUsername()));
    }
}
