package com.example.project.codeexecutor.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component("projectKeyGenerator")
public class CacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target,
                           Method method,
                           Object... params) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return email + ":" + params[0];
    }
}