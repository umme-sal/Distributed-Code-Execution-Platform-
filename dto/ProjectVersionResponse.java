package com.example.project.codeexecutor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ProjectVersionResponse {

    private Long id;

    private LocalDateTime createdAt;

    private String sourceCode;
}