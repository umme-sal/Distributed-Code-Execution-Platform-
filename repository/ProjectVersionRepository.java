package com.example.project.codeexecutor.repository;

import com.example.project.codeexecutor.entity.Project;
import com.example.project.codeexecutor.entity.ProjectVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectVersionRepository
        extends JpaRepository<ProjectVersion, Long> {

    List<ProjectVersion> findByProjectIdOrderByCreatedAtDesc(String projectId);

}