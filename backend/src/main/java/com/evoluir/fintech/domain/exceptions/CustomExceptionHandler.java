package com.evoluir.fintech.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Validation Error");

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        errors.put("details", fieldErrors);
        errors.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.NOT_FOUND.value());
        errors.put("error", "Resource Not Found");
        errors.put("message", ex.getMessage());
        errors.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errors.put("error", "Internal Server Error");
        errors.put("message", "An unexpected error occurred");
        errors.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }
}