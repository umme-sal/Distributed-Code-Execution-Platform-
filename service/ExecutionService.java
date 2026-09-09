package com.example.project.codeexecutor.service;

import com.example.project.codeexecutor.dto.ExecuteRequest;
import com.example.project.codeexecutor.dto.ExecuteResponse;
import com.example.project.codeexecutor.dto.ExecutionHistoryResponse;
import com.example.project.codeexecutor.entity.Execution;
import com.example.project.codeexecutor.entity.ExecutionStatus;
import com.example.project.codeexecutor.entity.Project;
import com.example.project.codeexecutor.entity.User;
import com.example.project.codeexecutor.queue.ExecutionQueueService;
import com.example.project.codeexecutor.queue.ExecutionTask;
import com.example.project.codeexecutor.repository.ExecutionRepository;
import com.example.project.codeexecutor.repository.ProjectRepository;
import com.example.project.codeexecutor.repository.UserRepository;
import com.example.project.codeexecutor.security.JwtService;
import com.example.project.codeexecutor.util.DockerUtil;
import com.example.project.codeexecutor.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExecutionService
{
    private final FileUtil fileUtil;
    private final DockerUtil dockerUtil;
    private final ExecutionRepository executionRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ProjectRepository projectRepository;
    private final ExecutionQueueService executionQueueService;

    public ExecuteResponse execute(ExecuteRequest request)
    {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        if (request.getProjectId() != null &&
                !request.getProjectId().isBlank())
        {
            Project project =
                    projectRepository.findById(request.getProjectId())
                            .orElseThrow(() ->
                                    new RuntimeException("Project not found"));

            if (!project.getUserId().equals(user.getId()))
            {
                throw new RuntimeException("Access Denied");
            }
        }

        Execution execution = new Execution();

        execution.setProjectId(request.getProjectId());
        execution.setLanguage(request.getLanguage());
        execution.setSourceCode(request.getSourceCode());

        execution.setOutput("");

        execution.setStatus(ExecutionStatus.QUEUED);

        execution.setExecutedAt(LocalDateTime.now());
        execution.setExpiresAt(LocalDateTime.now().plusDays(30));
        execution.setUser(user);

        executionRepository.save(execution);

        executionQueueService.submit(
                new ExecutionTask(
                        execution.getId(),
                        request,
                        email
                )
        );

        return new ExecuteResponse(
                execution.getId(),
                execution.getStatus()
        );
    }

    public void processExecution(Long executionId) {

        Execution execution = executionRepository
                .findById(executionId)
                .orElseThrow();

        execution.setStatus(ExecutionStatus.RUNNING);
        executionRepository.save(execution);

        try {

            Path dir = fileUtil.createExecutionDirectory();

            String output;

            switch (execution.getLanguage().toUpperCase()) {

                case "JAVA":
                    fileUtil.writeJavaFile(dir, execution.getSourceCode());
                    output = dockerUtil.executeJava(dir);
                    break;

                case "CPP":
                    fileUtil.writeCppFile(dir, execution.getSourceCode());
                    output = dockerUtil.executeCpp(dir);
                    break;

                case "PYTHON":
                    fileUtil.writePythonFile(dir, execution.getSourceCode());
                    output = dockerUtil.executePython(dir);
                    break;

                default:
                    throw new RuntimeException("Unsupported language");
            }

            execution.setOutput(output);
            execution.setStatus(ExecutionStatus.COMPLETED);

        } catch (Exception e) {

            execution.setOutput(e.getMessage());
            execution.setStatus(ExecutionStatus.FAILED);

        }

        executionRepository.save(execution);
    }
    public List<ExecutionHistoryResponse> getHistory(String token)
    {
        String email =
                jwtService.extractEmail(token);

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow();

        return executionRepository
                .findByUserOrderByExecutedAtDesc(user)
                .stream()
                .map(e ->
                        new ExecutionHistoryResponse(
                                e.getId(),
                                e.getLanguage(),
                                e.getSourceCode(),
                                e.getOutput(),
                                e.getExecutedAt(),
                                e.getStatus()
                        ))
                .toList();
    }

    public ExecutionHistoryResponse getExecution(
            Long id,
            String token)
    {

        String email =
                jwtService.extractEmail(token);

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow();

        Execution execution =
                executionRepository
                        .findByIdAndUser(id,user)
                        .orElseThrow();

        return new ExecutionHistoryResponse(
                execution.getId(),
                execution.getLanguage(),
                execution.getSourceCode(),
                execution.getOutput(),
                execution.getExecutedAt(),
                execution.getStatus()
        );
    }

    public void deleteExecution(
            Long id,
            String token)
    {

        String email =
                jwtService.extractEmail(token);

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow();

        Execution execution =
                executionRepository
                        .findByIdAndUser(id,user)
                        .orElseThrow();

        executionRepository.delete(execution);

    }
}