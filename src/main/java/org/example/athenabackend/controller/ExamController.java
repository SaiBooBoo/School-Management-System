package org.example.athenabackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dto.ExamDto;
import org.example.athenabackend.model.Subject;
import org.example.athenabackend.request.ExamCreationRequest;
import org.example.athenabackend.service.ExamService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;

    @GetMapping("/student/{studentId}")
    public List<ExamDto> getExamsByStudentId(@PathVariable Integer studentId){
        return examService.getByStudentId(studentId);
    }

    @GetMapping("/student/{studentId}/subject/{subject}")
    public List<ExamDto> getExamsByStudentAndSubject(@PathVariable Integer studentId,@PathVariable Subject subject){
        return examService.getByStudentAndSubject(studentId, subject);
    }

    @GetMapping("/date")
    public List<ExamDto> getExamsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ){
        return examService.getByDateRange(startDate, endDate);
    }

    @PostMapping
    public ResponseEntity<ExamDto> addExamToStudent(@RequestBody ExamDto exam){
        ExamDto saved = examService.addExam(exam);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/bulk")
    public ResponseEntity<String> assignExamToAll(@RequestBody ExamCreationRequest dto){
        examService.assignExamToAllStudents(dto);
        return ResponseEntity.ok("Exam assigned to all students.");
    }
}
