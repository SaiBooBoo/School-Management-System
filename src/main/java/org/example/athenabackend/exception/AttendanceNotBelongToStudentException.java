package org.example.athenabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AttendanceNotBelongToStudentException extends ResponseStatusException {

    public AttendanceNotBelongToStudentException(Integer studentId) {
        super(HttpStatus.BAD_REQUEST, String.format("Attendance does not belong to student %s",studentId));
    }
}
