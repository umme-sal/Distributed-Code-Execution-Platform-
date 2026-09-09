package com.example.project.codeexecutor.service;

import com.example.project.codeexecutor.dto.LoginRequest;
import com.example.project.codeexecutor.dto.LoginResponse;
import com.example.project.codeexecutor.dto.RegisterRequest;
import com.example.project.codeexecutor.entity.User;
import com.example.project.codeexecutor.repository.UserRepository;
import com.example.project.codeexecutor.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public String register(RegisterRequest request)
    {
        User user = new User();

        user.setUsername(request.getUsername());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return "User Registered Successfully";
    }

    public LoginResponse login(LoginRequest request)
    {
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()))
        {
            throw new RuntimeException(
                    "Invalid Password");
        }

        String token =
                jwtService.generateToken(
                        user.getEmail()
                );

        return new LoginResponse(token);
    }
}