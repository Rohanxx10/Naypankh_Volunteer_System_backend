package com.volunteer.controller;

import com.volunteer.dto.AuthDto;
import com.volunteer.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthDto.LoginResponse> login(@RequestBody AuthDto.LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDto.LoginResponse> register(@RequestBody AuthDto.RegisterRequest request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }
}
