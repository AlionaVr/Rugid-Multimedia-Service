package com.rugid.multimediaservice.adapter.in.exception.handler;

import com.rugid.multimediaservice.adapter.in.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException exception) {
        return createResponse("File Not Found", exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileSaveException.class)
    public ResponseEntity<?> handleFileSaveException(FileSaveException exception) {
        return createResponse("File Save Error", exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileDeleteException.class)
    public ResponseEntity<?> handleFileDeleteException(FileDeleteException exception) {
        return createResponse("File Delete Error", exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileReadBytesException.class)
    public ResponseEntity<?> handleFileReadBytesException(FileReadBytesException exception) {
        return createResponse("File Read Error", exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        return createResponse("File Size Exceeded", exception, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(DtoNotValidException.class)
    public ResponseEntity<?> handleDtoNotValidException(DtoNotValidException exception) {
        Map<String, Object> jsonResult = new LinkedHashMap<>();
        jsonResult.put("exception", "Validation Error");
        jsonResult.put("errors", exception.getErrorMessages());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(jsonResult);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception exception) {
        return createResponse("Internal Server Error", exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<?> createResponse(String exceptionName, Exception exception, HttpStatus status) {
        Map<String, Object> jsonResult = new LinkedHashMap<>();
        jsonResult.put("exception", exceptionName);
        jsonResult.put("message", exception.getMessage());

        return ResponseEntity
                .status(status)
                .body(jsonResult);
    }
}
