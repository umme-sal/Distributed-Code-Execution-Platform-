package com.example.project.codeexecutor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Table(name = "executions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Execution
{

    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectId;

    private String language;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String sourceCode;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String output;

    private LocalDateTime executedAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}