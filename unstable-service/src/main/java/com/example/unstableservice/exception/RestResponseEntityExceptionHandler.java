package com.example.unstableservice.exception;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            UnexpectedException.class
    })
    protected ResponseEntity<Object> handleUnexpectedException(RuntimeException ex, @NonNull WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @NonNull
    @Override
        protected ResponseEntity<Object> handleBindException(BindException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status,
                                                         @NonNull WebRequest request) {
        Map<String, String> errors = getErrors(ex.getBindingResult());
        log.warn("{}", errors);
        return new ResponseEntity<>(errors, headers, status);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body,
                                                             @NonNull HttpHeaders headers, @NonNull HttpStatus status,
                                                             @NonNull WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        log.warn("{}", errors);
        //return super.handleExceptionInternal(ex, body, headers, status, request);
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
