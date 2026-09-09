package com.example.project.codeexecutor.service;

import com.example.project.codeexecutor.config.RateLimitProperties;
import com.example.project.codeexecutor.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;
    private final RateLimitProperties rateLimitProperties;

    public void validateRequest(String email) {

        String key = "execute:" + email;

        Long count = redisTemplate.opsForValue().increment(key);

        if (count == null) {
            throw new RuntimeException("Unable to access Redis");
        }

        if (count == 1) {
            redisTemplate.expire(
                    key,
                    Duration.ofSeconds(rateLimitProperties.getWindowSeconds())
            );
        }

        if (count > rateLimitProperties.getMaxRequests()) {
            throw new RateLimitExceededException(
                    "Rate limit exceeded. Please try again later."
            );
        }
    }
}