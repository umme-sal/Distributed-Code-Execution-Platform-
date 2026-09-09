package com.example.project.codeexecutor.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project
{
    @Id
    private String id;

    // MySQL User ID
    private Long userId;

    private String title;

    private String language;

    private String sourceCode;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}