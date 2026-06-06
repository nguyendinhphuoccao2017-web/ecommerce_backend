package com.nguyendinhphuoccao.ecommerce.controller;

import com.nguyendinhphuoccao.ecommerce.dto.auth.*;
import com.nguyendinhphuoccao.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        log.info("=== START REGISTER ===");
        log.info("Email: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        if (response.getError() != null) {
            log.error("Register Error: {}", response.getError());
            return ResponseEntity.badRequest().body(response);
        }
        log.info("Register Success. AccessToken: {}", response.getToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        log.info("=== START LOGIN ===");
        log.info("Email: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        if (response.getError() != null) {
            log.error("Login Error: {}", response.getError());
            return ResponseEntity.badRequest().body(response);
        }
        log.info("Login Success. AccessToken: {}", response.getToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/social-login")
    public ResponseEntity<AuthResponse> socialLogin(@RequestBody SocialLoginRequest request) {
        log.info("=== START SOCIAL LOGIN ===");
        log.info("Provider: {}, Email: {}", request.getProvider(), request.getEmail());
        AuthResponse response = authService.socialLogin(request);
        log.info("Social Login Success. AccessToken: {}", response.getToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        log.info("=== START FORGOT PASSWORD ===");
        log.info("Email: {}", request.getEmail());
        authService.forgotPassword(request);
        log.info("Forgot Password email sent.");
        return ResponseEntity.ok().build();
    }
}
