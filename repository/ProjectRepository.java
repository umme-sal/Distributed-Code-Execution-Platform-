package com.example.project.codeexecutor.repository;

import com.example.project.codeexecutor.entity.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String>
{
    List<Project> findByUserId(Long userId);
}