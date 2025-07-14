package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameAlreadyExistException extends ResponseStatusException {

    public UsernameAlreadyExistException(String username) {
        super(HttpStatus.BAD_REQUEST, String.format("Username %s already exists", username));
    }
}
