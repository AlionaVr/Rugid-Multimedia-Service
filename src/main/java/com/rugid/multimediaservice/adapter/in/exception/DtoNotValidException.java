package com.rugid.multimediaservice.adapter.in.exception;

import java.util.Set;

public class DtoNotValidException extends RuntimeException {

    private final Set<String> errorMessages;

    public DtoNotValidException(Set<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Set<String> getErrorMessages() {
        return errorMessages;
    }
}
