package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StudentDoesNotExistInParentException extends ResponseStatusException {

    public StudentDoesNotExistInParentException(String studentName, String parentName) {
        super(HttpStatus.BAD_REQUEST, String.format("Student: %s doesn't exist in Parent: %s", studentName, parentName));
    }
}
