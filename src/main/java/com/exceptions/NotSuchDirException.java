package com.exceptions;

public class NotSuchDirException extends RuntimeException {
    public NotSuchDirException(String message) {
        super(message);
    }
}
