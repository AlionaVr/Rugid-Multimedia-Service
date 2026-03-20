package com.rugid.multimediaservice.adapter.in.exception;

public class FileReadBytesException extends RuntimeException {
    public FileReadBytesException() {
        super("Failed read file as bytes");
    }
}
