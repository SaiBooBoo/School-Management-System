package org.example.athenabackend.request;

import org.example.athenabackend.model.Subject;

import java.time.LocalDate;

public record ExamCreationRequest (
        String examName,
        Subject subject,
        LocalDate examDate
){
}
