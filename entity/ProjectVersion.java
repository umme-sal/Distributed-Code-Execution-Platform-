package com.example.project.codeexecutor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_versions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // MongoDB Project ID
    @Column(nullable = false)
    private String projectId;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String sourceCode;

    private LocalDateTime createdAt;
}