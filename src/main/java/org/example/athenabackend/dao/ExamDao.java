package org.example.athenabackend.dao;

import org.example.athenabackend.entity.Exam;
import org.example.athenabackend.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExamDao extends JpaRepository<Exam, Integer> {
    List<Exam> findByStudentId(int studentId);
    List<Exam> findByStudentIdAndSubject(int studentId, Subject subject);
    List<Exam> findBySubject(Subject subject);
    List<Exam> findByExamDateBetween(LocalDate start, LocalDate end);
}
