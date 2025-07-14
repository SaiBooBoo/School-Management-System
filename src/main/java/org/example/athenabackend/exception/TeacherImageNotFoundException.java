package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TeacherImageNotFoundException extends ResponseStatusException {

    public TeacherImageNotFoundException(Integer id) {
        super(HttpStatus.NOT_FOUND, String.format("Teacher with id: %s image not found",id));
    }
}
