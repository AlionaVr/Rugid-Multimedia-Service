package com.rugid.multimediaservice.domain.core.exception;

public class FileDeleteException extends RuntimeException {
    public FileDeleteException(String fileId, Throwable cause) {
        super("Failed to delete file: " + fileId, cause);
    }
}