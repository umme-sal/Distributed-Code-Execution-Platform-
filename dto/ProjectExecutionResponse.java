package com.example.project.codeexecutor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProjectExecutionResponse
{
    private Long id;

    private String output;

    private LocalDateTime executedAt;
}