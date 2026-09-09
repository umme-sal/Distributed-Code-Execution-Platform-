package com.example.project.codeexecutor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecuteRequest
{
    private String language;

    private String sourceCode;

    // optional
    private String projectId;
}