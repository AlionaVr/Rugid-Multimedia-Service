package com.rugid.multimediaservice.domain.core.exception;

import lombok.Getter;

import java.util.Set;

@Getter
public class DtoNotValidException extends RuntimeException {

    private final Set<String> errorMessages;

    public DtoNotValidException(Set<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

}
