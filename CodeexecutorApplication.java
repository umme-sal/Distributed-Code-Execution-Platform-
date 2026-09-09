package com.example.project.codeexecutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CodeexecutorApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(CodeexecutorApplication.class, args);
	}

}
