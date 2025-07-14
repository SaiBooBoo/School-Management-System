package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TeacherNotFoundException extends ResponseStatusException {
    public TeacherNotFoundException(String name) {
        super(HttpStatus.BAD_REQUEST, String.format("Teacher with name %s not found", name));
    }

    public TeacherNotFoundException(Integer id) {
        super(HttpStatus.BAD_REQUEST, String.format("Teacher with id %s not found", id));
    }
}
