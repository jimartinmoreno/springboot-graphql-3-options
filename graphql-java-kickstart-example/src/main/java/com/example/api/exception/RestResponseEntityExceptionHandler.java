package com.example.api.exception;

import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.rmi.UnexpectedException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            RetryableException.class
    })
    protected ResponseEntity<Object> handleUnexpectedException(RuntimeException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, @NotNull HttpHeaders headers,
                                                             @NotNull HttpStatusCode statusCode, @NotNull WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        log.warn("{}", errors);
        //return super.handleExceptionInternal(ex, body, headers, status, request);
        return new ResponseEntity<>(errors, headers, statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatusCode status, @NotNull WebRequest request) {
        Map<String, String> errors = getErrors(ex.getBindingResult());
        log.warn("{}", errors);
        return new ResponseEntity<>(errors, headers, status);
    }

    private Map<String, String> getErrors(BindingResult ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                errors.put(fieldName, errorMessage);
            } else {
                errors.put(error.getObjectName(), errorMessage);
            }
        });
        return errors;
    }
}
