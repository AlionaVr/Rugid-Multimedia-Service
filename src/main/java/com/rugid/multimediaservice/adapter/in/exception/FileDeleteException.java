package com.rugid.multimediaservice.adapter.in.exception;

public class FileDeleteException extends RuntimeException {
    public FileDeleteException(String fileId, Throwable cause) {
        super("Failed to delete file: " + fileId, cause);
    }
}