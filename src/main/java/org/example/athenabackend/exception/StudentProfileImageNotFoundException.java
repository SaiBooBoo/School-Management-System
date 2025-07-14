package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StudentProfileImageNotFoundException extends ResponseStatusException {
    public StudentProfileImageNotFoundException(Integer id) {
        super(HttpStatus.BAD_REQUEST, String.format("Student with id: %s profile image not found.", id));
    }
}
