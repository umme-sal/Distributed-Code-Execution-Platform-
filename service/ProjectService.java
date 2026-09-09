package com.example.project.codeexecutor.service;

import com.example.project.codeexecutor.dto.*;
import com.example.project.codeexecutor.entity.Project;
import com.example.project.codeexecutor.entity.ProjectVersion;
import com.example.project.codeexecutor.entity.User;
import com.example.project.codeexecutor.exception.ResourceNotFoundException;
import com.example.project.codeexecutor.repository.ExecutionRepository;
import com.example.project.codeexecutor.repository.ProjectRepository;
import com.example.project.codeexecutor.repository.ProjectVersionRepository;
import com.example.project.codeexecutor.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ExecutionRepository executionRepository;
    private final ProjectVersionRepository projectVersionRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(ProjectService.class);

    public ProjectResponse createProject(ProjectRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        Project project = new Project();

        project.setUserId(user.getId());
        project.setTitle(request.getTitle());
        project.setLanguage(request.getLanguage());
        project.setSourceCode(request.getSourceCode());
        project.setCreatedAt(LocalDateTime.now());
        project.setExpiresAt(LocalDateTime.now().plusDays(30));

        Project saved = projectRepository.save(project);

        return new ProjectResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getLanguage()
        );
    }

    public List<ProjectResponse> getProjects() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        return projectRepository.findByUserId(user.getId())
                .stream()
                .map(project -> new ProjectResponse(
                        project.getId(),
                        project.getTitle(),
                        project.getLanguage()
                ))
                .toList();
    }

    @Cacheable(
            value = "projects",
            keyGenerator = "projectKeyGenerator"
    )
    public ProjectDetailsResponse getProject(String id) {

        logger.info("Loaded project {}", id);

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        Project project = projectRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found"));

        if (!project.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access Denied");
        }

        return new ProjectDetailsResponse(
                project.getId(),
                project.getTitle(),
                project.getLanguage(),
                project.getSourceCode()
        );
    }

    public List<ProjectExecutionResponse> getProjectExecutions(String projectId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found"));

        if (!project.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access Denied");
        }

        return executionRepository
                .findByProjectIdOrderByExecutedAtDesc(projectId)
                .stream()
                .map(e -> new ProjectExecutionResponse(
                        e.getId(),
                        e.getOutput(),
                        e.getExecutedAt()
                ))
                .toList();
    }

    @Transactional
    @CacheEvict(
            value = "projects",
            keyGenerator = "projectKeyGenerator"
    )
    public ProjectResponse updateProject(String projectId, ProjectRequest request) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // Save current version before updating
        ProjectVersion version = ProjectVersion.builder()
                .projectId(project.getId())
                .sourceCode(project.getSourceCode())
                .createdAt(LocalDateTime.now())
                .build();

        projectVersionRepository.save(version);

        // Update project
        project.setTitle(request.getTitle());
        project.setLanguage(request.getLanguage());
        project.setSourceCode(request.getSourceCode());

        project = projectRepository.save(project);

        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getLanguage()
        );
    }

    @CacheEvict(
            value = "projects",
            keyGenerator = "projectKeyGenerator"
    )
    public void deleteProject(String id) {

        Project project = projectRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found"));

        projectRepository.delete(project);
    }

    public List<ProjectVersionResponse> getVersions(String projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return projectVersionRepository
                .findByProjectIdOrderByCreatedAtDesc(projectId)
                .stream()
                .map(version -> new ProjectVersionResponse(
                        version.getId(),
                        version.getCreatedAt(),
                        version.getSourceCode()
                ))
                .toList();
    }

    @Transactional
    public String restoreVersion(Long versionId) {

        ProjectVersion version = projectVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("Version not found"));

        Project project = projectRepository
                .findById(version.getProjectId())
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        // Save current project as a new version
        ProjectVersion backup = ProjectVersion.builder()
                .projectId(project.getId())
                .sourceCode(project.getSourceCode())
                .createdAt(LocalDateTime.now())
                .build();

        projectVersionRepository.save(backup);

        // Restore selected version
        project.setSourceCode(version.getSourceCode());

        projectRepository.save(project);

        return "Version restored successfully.";
    }

}