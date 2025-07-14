package org.example.athenabackend.util;

import org.example.athenabackend.dto.ExamDto;
import org.example.athenabackend.entity.Exam;
import org.example.athenabackend.entity.Student;

public class ExamUtil {
    public static ExamDto toExamDto(Exam exam){
        return new ExamDto(
                exam.getId(),
                exam.getStudent().getId(),
                exam.getExamName(),
                exam.getSubject(),
                exam.getExamDate(),
                exam.getScore(),
                exam.getMaxScore(),
                exam.getGrade(),
                exam.getRemarks()
        );
    }

    public static Exam toExam(ExamDto dto, Student student){
        return new Exam(
                student,
                dto.getExamName(),
                dto.getSubject(),
                dto.getExamDate(),
                dto.getScore(),
                dto.getMaxScore(),
                dto.getGrade(),
                dto.getRemarks()
        );
    }
}
