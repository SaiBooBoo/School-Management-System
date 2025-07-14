package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StudentDoesNotExistInTeacherException extends ResponseStatusException {

    public StudentDoesNotExistInTeacherException(String studentName, String teacherName) {
        super(HttpStatus.BAD_REQUEST, String.format("Student: %s doesn't exist in Teacher: %s", studentName, teacherName));
    }
}
