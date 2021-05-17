package com.github.ahenteti.dummyserver.exception;

public class DummyServerException extends RuntimeException {
    public DummyServerException(String message) {
        super(message);
    }

    public DummyServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
