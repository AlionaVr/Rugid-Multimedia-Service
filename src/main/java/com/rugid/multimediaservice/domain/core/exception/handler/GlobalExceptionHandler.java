package com.rugid.multimediaservice.domain.core.exception.handler;

import com.rugid.multimediaservice.domain.core.exception.DtoNotValidException;
import com.rugid.multimediaservice.domain.core.exception.FileDeleteException;
import com.rugid.multimediaservice.domain.core.exception.FileNotFoundException;
import com.rugid.multimediaservice.domain.core.exception.FileReadException;
import com.rugid.multimediaservice.domain.core.exception.FileSaveException;
import com.rugid.multimediaservice.domain.core.exception.FileUploadReadException;
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

    @ExceptionHandler(FileUploadReadException.class)
    public ResponseEntity<?> handleFileUploadReadException(FileUploadReadException exception) {
        return createResponse("File Upload Error", exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileReadException.class)
    public ResponseEntity<?> handleFileReadException(FileReadException exception) {
        return createResponse("File Read Error", exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        return createResponse("File Size Exceeded", exception, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException exception) {
        return createResponse("Invalid Argument", exception, HttpStatus.BAD_REQUEST);
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
