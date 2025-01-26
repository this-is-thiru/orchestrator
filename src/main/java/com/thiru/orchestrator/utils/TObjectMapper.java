package com.thiru.orchestrator.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class TObjectMapper {

    private static final ObjectMapper objectMapper = getObjectMapper();

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    public static <T> T copy(Object source, Class<T> targetClass) {
        return readValue(writeValueAsString(source), targetClass);
    }

    public static <T, R> List<T> copyCollection(List<R> source, Class<T> elementClass) {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, elementClass);
        return readValue(writeValueAsString(source), javaType);
    }

    private static <T> T readValue(String content, JavaType targetClass) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        try {
            return objectMapper.readValue(content, targetClass);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static <T> T readValue(String content, Class<T> targetClass) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        try {
            return objectMapper.readValue(content, targetClass);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String writeValueAsString(Object source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
