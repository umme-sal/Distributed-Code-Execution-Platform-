package com.example.project.codeexecutor.service;

import com.example.project.codeexecutor.dto.AIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    private final RestClient restClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    // ========================= COMMON GEMINI CALL =========================

    private AIResponse askGemini(String prompt) {

        Map<String, Object> requestBody = Map.of(
                "contents",
                List.of(
                        Map.of(
                                "parts",
                                List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        Map response = restClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        return new AIResponse(extractText(response));
    }

    // ========================= EXPLAIN CODE =========================

    public AIResponse explainCode(String language, String code) {

        String prompt = """
                Explain the following %s code.

                Mention:
                1. Purpose
                2. Algorithm
                3. Time Complexity
                4. Space Complexity

                Code:

                %s
                """.formatted(language, code);

        return askGemini(prompt);
    }

    // ========================= DEBUG CODE =========================

    public AIResponse debugCode(String language, String code) {

        String prompt = """
                Find bugs in the following %s code.

                Return:

                1. Bugs
                2. Reason
                3. Suggested Fix

                Code:

                %s
                """.formatted(language, code);

        return askGemini(prompt);
    }

    // ========================= OPTIMIZE CODE =========================

    public AIResponse optimizeCode(String language, String code) {

        String prompt = """
                Optimize the following %s code.

                Mention:

                1. Performance Improvements
                2. Memory Improvements
                3. Better Coding Practices

                Code:

                %s
                """.formatted(language, code);

        return askGemini(prompt);
    }

    // ========================= COMPLEXITY ANALYSIS =========================

    public AIResponse complexity(String language, String code) {

        String prompt = """
                Analyze the following %s code.

                Return only:

                Time Complexity

                Space Complexity

                Explanation

                Code:

                %s
                """.formatted(language, code);

        return askGemini(prompt);
    }

    // ========================= TEST CASE GENERATION =========================

    public AIResponse generateTests(String language, String code) {

        String prompt = """
                Generate test cases for the following %s code.

                Include:

                1. Normal Test Cases
                2. Boundary Test Cases
                3. Edge Cases
                4. Invalid Inputs

                Code:

                %s
                """.formatted(language, code);

        return askGemini(prompt);
    }

    // ========================= AI CHAT =========================

    public AIResponse chat(String question) {

        return askGemini(question);
    }

    // ========================= AI CODE REVIEW =========================

    public AIResponse reviewCode(String language, String code) {

        String prompt = """
                You are a Senior Software Engineer.

                Review the following %s code.

                Give:

                1. Bugs
                2. Security Issues
                3. Performance Issues
                4. Clean Code Suggestions
                5. Best Practices
                6. Time Complexity
                7. Space Complexity
                8. Score out of 10
                9. Overall Summary

                Code:

                %s
                """.formatted(language, code);

        return askGemini(prompt);
    }

    // ========================= RESPONSE PARSER =========================

    @SuppressWarnings("unchecked")
    private String extractText(Map response) {

        try {

            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) response.get("candidates");

            Map<String, Object> content =
                    (Map<String, Object>) candidates.get(0).get("content");

            List<Map<String, Object>> parts =
                    (List<Map<String, Object>>) content.get("parts");

            return (String) parts.get(0).get("text");

        } catch (Exception e) {

            return "Unable to parse Gemini response.";

        }
    }
}