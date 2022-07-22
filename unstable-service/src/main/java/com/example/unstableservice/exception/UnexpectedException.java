package com.example.unstableservice.exception;

import java.util.UUID;

public class UnexpectedException extends RuntimeException {
    public UnexpectedException() {
        super("Oups something bad happened!");
    }
}
