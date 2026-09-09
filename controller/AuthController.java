package com.example.project.codeexecutor.controller;

import com.example.project.codeexecutor.dto.LoginRequest;
import com.example.project.codeexecutor.dto.LoginResponse;
import com.example.project.codeexecutor.dto.RegisterRequest;
import com.example.project.codeexecutor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request)
    {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request)
    {
        return authService.login(request);
    }
}