package com.bank.msdebt.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas unitarias del manejador global de excepciones.
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleIllegalArgument_returnsBadRequestWithMessage() {
        Mono<ResponseEntity<Map<String, String>>> result =
                handler.handleIllegalArgument(new IllegalArgumentException("El cliente es obligatorio"));

        ResponseEntity<Map<String, String>> response = result.block();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El cliente es obligatorio", response.getBody().get("error"));
    }
}
