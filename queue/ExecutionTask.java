package com.example.project.codeexecutor.queue;

import com.example.project.codeexecutor.dto.ExecuteRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExecutionTask {

    private Long executionId;

    private ExecuteRequest request;

    private String email;
}