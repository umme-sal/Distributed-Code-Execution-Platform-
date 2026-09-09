package com.example.project.codeexecutor.scheduler;

import com.example.project.codeexecutor.repository.ExecutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ExecutionCleanupScheduler
{
    private final ExecutionRepository executionRepository;

    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredExecutions()
    {
        executionRepository.deleteByExpiresAtBefore(
                LocalDateTime.now()
        );

        System.out.println("Expired executions deleted.");
    }
}