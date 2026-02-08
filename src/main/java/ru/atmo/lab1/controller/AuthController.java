package ru.atmo.lab1.controller;

import lombok.RequiredArgsConstructor;
import ru.atmo.lab1.dto.JwtAuthResponse;
import ru.atmo.lab1.dto.LoginRequest;
import ru.atmo.lab1.dto.SignUpRequest;
import ru.atmo.lab1.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public JwtAuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/sign-up")
    public JwtAuthResponse signUp(@Valid @RequestBody SignUpRequest request) {
        return authService.signUp(request);
    }
}
