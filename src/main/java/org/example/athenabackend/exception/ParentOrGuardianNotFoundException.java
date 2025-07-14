package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ParentOrGuardianNotFoundException extends ResponseStatusException {
    public ParentOrGuardianNotFoundException(Integer id) {
        super(HttpStatus.BAD_REQUEST, String.format("Parent Or Guardian Not Found with id: %d", id));
    }

    public ParentOrGuardianNotFoundException(String parentOrGuardianName) {
        super(HttpStatus.BAD_REQUEST, String.format("Parent Or Guardian Not Found with name: %s", parentOrGuardianName));
    }
}
