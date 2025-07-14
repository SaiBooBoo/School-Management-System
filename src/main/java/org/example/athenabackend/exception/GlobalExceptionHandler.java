package org.example.athenabackend.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle custom exceptions
    @ExceptionHandler(value = {
            StudentNotFoundException.class,
            UsernameAlreadyExistException.class,
            TeacherNotFoundException.class,
            ParentOrGuardianNotFoundException.class,
            ParentAlreadyRemovedException.class,
            StudentDoesNotExistInParentException.class,
            Exception.class,
            AttendanceNotFoundException.class,
            AttendanceNotBelongToStudentException.class,
            StudentProfileImageNotFoundException.class
    })
    public ResponseEntity<Object> handleCustomExceptions(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex,
                message(ex),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request);
    }

    // Handle MaxUploadSizeExceededException by overriding parent method

    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (ex instanceof MaxUploadSizeExceededException) {
            return handleMaxSizeException((MaxUploadSizeExceededException) ex);
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    // Specific handler for file size exception
    private ResponseEntity<Object> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        String errorMessage = "File size exceeds the maximum limit";
        ApiError apiError = new ApiError(errorMessage, HttpStatus.PAYLOAD_TOO_LARGE.value(), LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    // Utility method for error messages
    private ApiError message(Exception ex) {
        String message = Objects.nonNull(ex) ? ex.getMessage() : "Unknown Message";
        return new ApiError(message, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }
}