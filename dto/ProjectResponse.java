package com.example.project.codeexecutor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectResponse
{
    private String id;

    private String title;

    private String language;
}