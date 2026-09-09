package com.example.project.codeexecutor.dto;

import com.example.project.codeexecutor.entity.ExecutionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExecuteResponse {

    private Long executionId;

    private ExecutionStatus status;

}