package com.example.project.codeexecutor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectRequest
{
    private String title;

    private String language;

    private String sourceCode;
}