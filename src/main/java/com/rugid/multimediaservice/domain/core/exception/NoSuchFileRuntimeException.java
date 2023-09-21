package com.rugid.multimediaservice.domain.core.exception;

public class NoSuchFileRuntimeException extends RuntimeException {

    public NoSuchFileRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchFileRuntimeException(Throwable cause) {
        super(cause);
    }

    public NoSuchFileRuntimeException(String message) {
        super(message);
    }
}
