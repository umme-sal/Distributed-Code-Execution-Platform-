package com.example.project.codeexecutor.queue;

import com.example.project.codeexecutor.config.ExecutionProperties;
import com.example.project.codeexecutor.service.ExecutionService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
public class ExecutionWorker {

    private final ExecutionQueueService executionQueueService;
    private final ExecutionService executionService;
    private final ExecutorService executionExecutor;
    private final ExecutionProperties executionProperties;

    @PostConstruct
    public void startWorkers() {

        int workers = executionProperties.getWorkerCount();

        for (int i = 0; i < workers; i++) {

            executionExecutor.submit(() -> {

                while (!Thread.currentThread().isInterrupted()) {

                    try {

                        ExecutionTask task =
                                executionQueueService.take();

                        executionService.processExecution(
                                task.getExecutionId()
                        );

                    }
                    catch (InterruptedException e) {

                        Thread.currentThread().interrupt();

                    }
                    catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            });

        }

        System.out.println(
                workers +
                        " Execution Workers Started..."
        );

    }

    @PreDestroy
    public void shutdown() {

        executionExecutor.shutdown();

        System.out.println(
                "Execution Workers Stopped."
        );

    }

}