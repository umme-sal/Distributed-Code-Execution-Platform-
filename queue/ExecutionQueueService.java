package com.example.project.codeexecutor.queue;

import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ExecutionQueueService {

    private final BlockingQueue<ExecutionTask> queue =
            new LinkedBlockingQueue<>();

    public void submit(ExecutionTask task) {
        queue.offer(task);
    }

    public ExecutionTask take() throws InterruptedException {
        return queue.take();
    }

    public int size() {
        return queue.size();
    }
}