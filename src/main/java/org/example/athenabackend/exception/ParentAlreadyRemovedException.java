package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ParentAlreadyRemovedException extends ResponseStatusException {
    public ParentAlreadyRemovedException(Integer id) {
        super(HttpStatus.BAD_REQUEST, "Parent with id " + id + " is already removed");
    }
}
