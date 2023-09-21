package com.rugid.multimediaservice.adapter.in.exception.handler;

import com.rugid.multimediaservice.adapter.in.exception.DtoNotValidException;
import com.rugid.multimediaservice.domain.core.exception.IORuntimeException;
import com.rugid.multimediaservice.domain.core.exception.NoSuchFileRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IORuntimeException.class)
    public ResponseEntity<?> handleException(IORuntimeException exception) {
        return createResponse("IO exception", exception);
    }

    @ExceptionHandler(NoSuchFileRuntimeException.class)
    public ResponseEntity<?> handleException(NoSuchFileRuntimeException exception) {
        return createResponse("No such file exception", exception);
    }

    @ExceptionHandler(DtoNotValidException.class)
    public ResponseEntity<?> handleException(DtoNotValidException exception) {
        Map<String, Object> jsonResult = new LinkedHashMap<>();
        jsonResult.put("Exception", "Json validation exception");
        jsonResult.put("Violations", exception.getErrorMessages());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(jsonResult);
    }

    private ResponseEntity<?> createResponse(String exceptionName, Exception exception) {
        Map<String, Object> jsonResult = new LinkedHashMap<>();
        jsonResult.put("Exception", exceptionName);
        jsonResult.put("Message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(jsonResult);
    }
}
