package com.thiru.orchestrator.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class TObjectMapperTest {

    @Test
    void copy() {
        String s1 = TObjectMapper.copy("", String.class);
        Assertions.assertEquals("", s1);
    }

    @Test
    void copyCollection() {
        String s = null;
        List<String> s1 = TObjectMapper.copyCollection(List.of(), String.class);
        Assertions.assertEquals(0, s1.size());
    }

    @Test
    void readValue() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> TObjectMapper.readValue(null, String.class));
    }

    @Test
    void readValue1() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> TObjectMapper.readValue(null, javaType));
    }

    @Test
    void writeValueAsString() {
        String s = null;
        List<String> s1 = TObjectMapper.copyCollection(List.of(), String.class);
        Assertions.assertEquals(0, s1.size());
    }
}