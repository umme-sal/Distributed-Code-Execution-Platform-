package com.example.project.codeexecutor.controller;

import com.example.project.codeexecutor.dto.*;
import com.example.project.codeexecutor.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController
{
    private final ProjectService projectService;

    @PostMapping
    public ProjectResponse createProject(
            @RequestBody ProjectRequest request)
    {
        return projectService.createProject(request);
    }

    @GetMapping
    public List<ProjectResponse> getProjects() {
        return projectService.getProjects();
    }

    @GetMapping("/{id}")
    public ProjectDetailsResponse getProject(
            @PathVariable String id)
    {
        return projectService
                .getProject(id);
    }

    @GetMapping("/{projectId}/executions")
    public List<ProjectExecutionResponse> getExecutions(
            @PathVariable String projectId)
    {
        return projectService.getProjectExecutions(projectId);
    }

    @PutMapping("/{id}")
    public ProjectResponse updateProject(
            @PathVariable String id,
            @RequestBody ProjectRequest request)
    {
        return projectService.updateProject(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(
            @PathVariable String id)
    {
        projectService.deleteProject(id);
    }

    @GetMapping("/{projectId}/versions")
    public ResponseEntity<List<ProjectVersionResponse>> getVersions(
            @PathVariable String projectId) {

        return ResponseEntity.ok(
                projectService.getVersions(projectId)
        );
    }

    @PostMapping("/versions/{versionId}/restore")
    public ResponseEntity<String>
    restoreVersion(@PathVariable Long versionId) {

        return ResponseEntity.ok(
                projectService.restoreVersion(versionId)
        );
    }
}