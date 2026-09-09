package com.example.project.codeexecutor.dto;

import com.example.project.codeexecutor.entity.ExecutionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ExecutionHistoryResponse
{

    private Long id;

    private String language;

    private String sourceCode;

    private String output;

    private LocalDateTime executedAt;

    private ExecutionStatus status;
}