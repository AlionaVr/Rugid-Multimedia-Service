package com.rugid.multimediaservice.adapter.in.rest.validator;

import com.rugid.multimediaservice.adapter.in.exception.DtoNotValidException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JsonDtoValidator<T> {

    private final Validator validator;

    public JsonDtoValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(T objectToValidate) {
        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);

        if (!violations.isEmpty()) {
            Set<String> errorMessages = violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());

            throw new DtoNotValidException(errorMessages);
        }
    }
}
