package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class ClassroomNotFoundException extends ResponseStatusException {
    public ClassroomNotFoundException(Integer id) {
        super(HttpStatus.BAD_REQUEST, String.format("Classroom with id %s not found", id));
    }
}
