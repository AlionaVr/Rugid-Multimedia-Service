package com.rugid.multimediaservice.adapter.in.exception;

public class FileSaveException extends RuntimeException {
    public FileSaveException(String filename, Throwable cause) {
        super("Failed to save file: " + filename, cause);
    }
}