package com.rugid.multimediaservice.domain.core.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
        super("File not found");
    }
}
