package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class ClassroomAlreadyExistsException extends ResponseStatusException {
    public ClassroomAlreadyExistsException(String name) {
        super(HttpStatus.CONFLICT, "Classroom already exists");
    }
}
