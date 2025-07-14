package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StudentNotFoundException extends ResponseStatusException {
    public StudentNotFoundException(String name) {
        super(HttpStatus.BAD_REQUEST, String.format("Student name with %s is not found", name));
    }

    public StudentNotFoundException(Integer id){
        super(HttpStatus.BAD_REQUEST, String.format("Student id with %s is not found", id));
    }

}
