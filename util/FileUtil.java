package com.example.project.codeexecutor.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class FileUtil
{
    public Path createExecutionDirectory() throws IOException
    {
        String folderName =
                "execution_" + UUID.randomUUID();

        return Files.createTempDirectory(folderName);
    }

    public Path writeJavaFile(
            Path directory,
            String sourceCode
    ) throws IOException
    {
        Path javaFile =
                directory.resolve("Main.java");

        Files.writeString(
                javaFile,
                sourceCode
        );

        return javaFile;
    }

    public void deleteDirectory(
            Path directory
    ) throws IOException
    {
        Files.walk(directory)
                .sorted((a,b) -> b.compareTo(a))
                .forEach(path ->
                {
                    try
                    {
                        Files.delete(path);
                    }
                    catch(Exception ignored)
                    {

                    }
                });
    }

    public String getDirectoryPath(
            Path directory
    )
    {
        return directory.toAbsolutePath()
                .toString();
    }
    public void writeCppFile(
            Path directory,
            String sourceCode)
            throws Exception
    {
        Files.writeString(
                directory.resolve("main.cpp"),
                sourceCode
        );
    }

    public void writePythonFile(
            Path directory,
            String sourceCode)
            throws Exception
    {
        Files.writeString(
                directory.resolve("main.py"),
                sourceCode
        );
    }
}

