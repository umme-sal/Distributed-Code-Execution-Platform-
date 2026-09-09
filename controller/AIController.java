package com.example.project.codeexecutor.controller;

import com.example.project.codeexecutor.dto.AIRequest;
import com.example.project.codeexecutor.dto.AIResponse;
import com.example.project.codeexecutor.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    // ========================= EXPLAIN CODE =========================

    @PostMapping("/explain")
    public AIResponse explainCode(
            @RequestBody AIRequest request) {

        return aiService.explainCode(
                request.getLanguage(),
                request.getCode()
        );
    }

    // ========================= DEBUG CODE =========================

    @PostMapping("/debug")
    public AIResponse debugCode(
            @RequestBody AIRequest request) {

        return aiService.debugCode(
                request.getLanguage(),
                request.getCode()
        );
    }

    // ========================= OPTIMIZE CODE =========================

    @PostMapping("/optimize")
    public AIResponse optimizeCode(
            @RequestBody AIRequest request) {

        return aiService.optimizeCode(
                request.getLanguage(),
                request.getCode()
        );
    }

    // ========================= COMPLEXITY =========================

    @PostMapping("/complexity")
    public AIResponse complexity(
            @RequestBody AIRequest request) {

        return aiService.complexity(
                request.getLanguage(),
                request.getCode()
        );
    }

    // ========================= TEST CASES =========================

    @PostMapping("/testcases")
    public AIResponse generateTestCases(
            @RequestBody AIRequest request) {

        return aiService.generateTests(
                request.getLanguage(),
                request.getCode()
        );
    }

    // ========================= AI REVIEW =========================

    @PostMapping("/review")
    public AIResponse reviewCode(
            @RequestBody AIRequest request) {

        return aiService.reviewCode(
                request.getLanguage(),
                request.getCode()
        );
    }

    // ========================= AI CHAT =========================

    @PostMapping("/chat")
    public AIResponse chat(
            @RequestBody String question) {

        return aiService.chat(question);
    }

}