package com.rugid.multimediaservice.domain.core.exception;

public class FileUploadReadException extends RuntimeException {
    public FileUploadReadException(Throwable cause) {
        super("Failed to read uploaded file", cause);
    }
}
