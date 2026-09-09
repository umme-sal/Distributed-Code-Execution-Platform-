package com.example.project.codeexecutor.util;

import com.example.project.codeexecutor.config.ExecutionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DockerUtil {

    private final ExecutionProperties executionProperties;

    // ========================= JAVA =========================

    public String executeJava(Path directory) throws Exception {

        ProcessBuilder compileBuilder = new ProcessBuilder(
                "docker",
                "run",
                "--rm",
                "--network", "none",
                "--memory", "256m",
                "--cpus", "1",
                "--stop-timeout", "1",
                "-v",
                directory.toAbsolutePath() + ":/app",
                "-w",
                "/app",
                "eclipse-temurin:17",
                "javac",
                "Main.java"
        );

        Process compileProcess = compileBuilder.start();
        compileProcess.waitFor();

        String compileErrors = readStream(compileProcess.getErrorStream());

        if (compileProcess.exitValue() != 0) {
            return "Compilation Error:\n" + compileErrors;
        }

        ProcessBuilder runBuilder = new ProcessBuilder(
                "docker",
                "run",
                "--rm",
                "--network", "none",
                "--memory", "256m",
                "--cpus", "1",
                "--stop-timeout", "1",
                "-v",
                directory.toAbsolutePath() + ":/app",
                "-w",
                "/app",
                "eclipse-temurin:17",
                "java",
                "Main"
        );

        Process runProcess = runBuilder.start();

        if (!waitForProcess(runProcess)) {
            return timeoutMessage();
        }

        String output = readStream(runProcess.getInputStream());
        String runtimeErrors = readStream(runProcess.getErrorStream());

        if (runProcess.exitValue() != 0) {
            return "Runtime Error:\n" + runtimeErrors;
        }

        return output.isBlank() ? "Program executed successfully." : output;
    }

    // ========================= C++ =========================

    public String executeCpp(Path directory) throws Exception {

        ProcessBuilder compileBuilder = new ProcessBuilder(
                "docker",
                "run",
                "--rm",
                "--network", "none",
                "--memory", "256m",
                "--cpus", "1",
                "--stop-timeout", "1",
                "-v",
                directory.toAbsolutePath() + ":/app",
                "-w",
                "/app",
                "gcc:latest",
                "g++",
                "main.cpp",
                "-o",
                "main"
        );

        Process compileProcess = compileBuilder.start();
        compileProcess.waitFor();

        String compileErrors = readStream(compileProcess.getErrorStream());

        if (compileProcess.exitValue() != 0) {
            return "Compilation Error:\n" + compileErrors;
        }

        ProcessBuilder runBuilder = new ProcessBuilder(
                "docker",
                "run",
                "--rm",
                "--network", "none",
                "--memory", "256m",
                "--cpus", "1",
                "--stop-timeout", "1",
                "-v",
                directory.toAbsolutePath() + ":/app",
                "-w",
                "/app",
                "gcc:latest",
                "./main"
        );

        Process runProcess = runBuilder.start();

        if (!waitForProcess(runProcess)) {
            return timeoutMessage();
        }

        String output = readStream(runProcess.getInputStream());
        String runtimeErrors = readStream(runProcess.getErrorStream());

        if (runProcess.exitValue() != 0) {
            return "Runtime Error:\n" + runtimeErrors;
        }

        return output.isBlank() ? "Program executed successfully." : output;
    }

    // ========================= PYTHON =========================

    public String executePython(Path directory) throws Exception {

        ProcessBuilder runBuilder = new ProcessBuilder(
                "docker",
                "run",
                "--rm",
                "--network", "none",
                "--memory", "256m",
                "--cpus", "1",
                "--stop-timeout", "1",
                "-v",
                directory.toAbsolutePath() + ":/app",
                "-w",
                "/app",
                "python:3.12",
                "python",
                "main.py"
        );

        Process process = runBuilder.start();

        if (!waitForProcess(process)) {
            return timeoutMessage();
        }

        String output = readStream(process.getInputStream());
        String errors = readStream(process.getErrorStream());

        if (process.exitValue() != 0) {
            return "Runtime Error:\n" + errors;
        }

        return output.isBlank() ? "Program executed successfully." : output;
    }

    // ========================= COMMON METHODS =========================

    private boolean waitForProcess(Process process)
            throws InterruptedException {

        boolean finished = process.waitFor(
                executionProperties.getTimeoutSeconds(),
                TimeUnit.SECONDS
        );

        if (!finished) {
            process.destroyForcibly();
        }

        return finished;
    }

    private String timeoutMessage() {

        return "Execution timed out after "
                + executionProperties.getTimeoutSeconds()
                + " seconds.";
    }

    private String readStream(InputStream stream) throws Exception {

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(stream)
                );

        StringBuilder builder = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }

        return builder.toString();
    }
}