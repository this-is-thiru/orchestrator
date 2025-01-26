package com.thiru.orchestrator.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private static GlobalExceptionHandler exceptionHandler;

    @BeforeAll
    static void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFoundException() {
        ResponseEntity<String> response = exceptionHandler.handleNotFoundException(new NotFoundException("Product not found"));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleIllegalArgumentException() {
        ResponseEntity<String> response = exceptionHandler.handleIllegalArgumentException(new IllegalArgumentException("Invalid input"));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}