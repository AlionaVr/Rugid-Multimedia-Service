package com.rugid.multimediaservice.domain.core.exception;

public class FileReadException extends RuntimeException {
    public FileReadException(Throwable cause) {
        super("Failed to read file from storage", cause);
    }
}
