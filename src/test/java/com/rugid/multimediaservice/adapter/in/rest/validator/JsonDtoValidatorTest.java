package com.rugid.multimediaservice.adapter.in.rest.validator;

import com.rugid.multimediaservice.adapter.in.exception.DtoNotValidException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonDtoValidatorTest {

    @Mock
    private Validator validator;
    @InjectMocks
    private JsonDtoValidator<MockMultipartFile> jsonDtoValidator;


    @Test
    void testValidate_whenValidData_thenOk() {
        Set<ConstraintViolation<MockMultipartFile>> violationSet = new HashSet<>();
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "This is a test file".getBytes());
        when(validator.validate(mockFile)).thenReturn(violationSet);

        assertDoesNotThrow(() -> jsonDtoValidator.validate(mockFile));
    }

    @Test
    void testValidate_whenInvalidData_thenReturnedThrow() {
        Set<ConstraintViolation<MockMultipartFile>> violationSet = new HashSet<>();
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "This is a test file".getBytes());
        violationSet.add(mock(ConstraintViolation.class));
        when(validator.validate(mockFile)).thenReturn(violationSet);

        DtoNotValidException exception = assertThrows(DtoNotValidException.class,
                () -> jsonDtoValidator.validate(mockFile));

        Set<String> errorMessage = exception.getErrorMessages();

        assertNotNull(errorMessage);
    }
}