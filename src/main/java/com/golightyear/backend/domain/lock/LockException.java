package com.golightyear.backend.domain.lock;

public class LockException extends RuntimeException {
    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable throwable) {
        super(message, throwable);
    }
}