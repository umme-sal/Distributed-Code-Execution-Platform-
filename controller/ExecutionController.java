package com.example.project.codeexecutor.controller;

import com.example.project.codeexecutor.dto.ExecuteRequest;
import com.example.project.codeexecutor.dto.ExecuteResponse;
import com.example.project.codeexecutor.dto.ExecutionHistoryResponse;
import com.example.project.codeexecutor.service.ExecutionService;
import com.example.project.codeexecutor.service.RateLimiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/execute")
@RequiredArgsConstructor
public class ExecutionController
{
    private final ExecutionService executionService;
    private final RateLimiterService rateLimiterService;

    @PostMapping
    public ExecuteResponse execute(
            @RequestBody ExecuteRequest request)
    {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        rateLimiterService.validateRequest(email);

        return executionService.execute(request);
    }

    @GetMapping("/executions")
    public List<ExecutionHistoryResponse> history(
            @RequestHeader("Authorization")
            String authHeader)
    {

        String token =
                authHeader.substring(7);

        return executionService.getHistory(token);

    }

    @GetMapping("/executions/{id}")
    public ExecutionHistoryResponse getExecution(
            @PathVariable Long id,
            @RequestHeader("Authorization")
            String authHeader)
    {

        String token =
                authHeader.substring(7);

        return executionService.getExecution(id,token);

    }

    @DeleteMapping("/executions/{id}")
    public void deleteExecution(
            @PathVariable Long id,
            @RequestHeader("Authorization")
            String authHeader)
    {

        String token =
                authHeader.substring(7);

        executionService.deleteExecution(id,token);

    }
}