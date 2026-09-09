package com.example.project.codeexecutor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    @Bean
    public ExecutorService executionExecutor() {

        // 2 worker threads
        return Executors.newFixedThreadPool(2);

    }

}