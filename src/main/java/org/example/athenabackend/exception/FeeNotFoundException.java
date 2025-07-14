package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class FeeNotFoundException extends ResponseStatusException {


    public FeeNotFoundException(Integer id) {
        super(HttpStatus.BAD_REQUEST, String.format("Fees with id:%s not found", id));
    }
}
