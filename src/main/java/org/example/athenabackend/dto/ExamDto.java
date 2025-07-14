package org.example.athenabackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.athenabackend.model.Subject;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExamDto {
    private Integer id;
    private Integer studentId;
    private String examName;
    private Subject subject;
    private LocalDate examDate;
    private Double score;
    private Double maxScore;
    private String grade;
    private String remarks;
}
