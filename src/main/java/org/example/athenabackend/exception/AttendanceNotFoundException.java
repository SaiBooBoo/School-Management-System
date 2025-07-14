package org.example.athenabackend.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class AttendanceNotFoundException extends ResponseStatusException {
    public AttendanceNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
