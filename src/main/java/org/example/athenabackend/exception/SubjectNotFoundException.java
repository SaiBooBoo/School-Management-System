package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SubjectNotFoundException extends ResponseStatusException {
    public SubjectNotFoundException(Integer id) {
        super(HttpStatus.NOT_FOUND, String.format("Subject with id %d not found", id));
    }

}
