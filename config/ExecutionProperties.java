package com.example.project.codeexecutor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "execution")
public class ExecutionProperties {

    private int timeoutSeconds;

    private int workerCount;

}