package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SubjectAlreadyExistsException extends ResponseStatusException {
    public SubjectAlreadyExistsException(String subjectName) {
        super (HttpStatus.BAD_REQUEST,  String.format("Subject with name %s already exists", subjectName));

    }
}
