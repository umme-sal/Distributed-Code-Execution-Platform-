package com.example.project.codeexecutor.repository;

import com.example.project.codeexecutor.entity.Execution;
import com.example.project.codeexecutor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExecutionRepository extends JpaRepository<Execution,Long>
{

    List<Execution> findByUserOrderByExecutedAtDesc(User user);

    Optional<Execution> findByIdAndUser(Long id, User user);

    void deleteByExpiresAtBefore(LocalDateTime time);

    List<Execution> findByProjectIdOrderByExecutedAtDesc(String projectId);

}