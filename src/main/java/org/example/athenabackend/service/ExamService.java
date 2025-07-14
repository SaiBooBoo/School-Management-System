package org.example.athenabackend.service;

import lombok.AllArgsConstructor;
import org.example.athenabackend.dao.ExamDao;
import org.example.athenabackend.dao.StudentDao;
import org.example.athenabackend.dto.ExamDto;
import org.example.athenabackend.entity.Exam;
import org.example.athenabackend.entity.Student;
import org.example.athenabackend.exception.StudentNotFoundException;
import org.example.athenabackend.model.Subject;
import org.example.athenabackend.request.ExamCreationRequest;
import org.example.athenabackend.util.ExamUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ExamService {
    private final ExamDao examDao;
    private final StudentDao studentDao;

    public List<ExamDto> getByStudentId(Integer studentId){
        return examDao.findByStudentId(studentId).stream()
                .map(ExamUtil::toExamDto)
                .toList();
    }

    public List<ExamDto> getByStudentAndSubject(Integer studentId, Subject subject){
        return examDao.findByStudentIdAndSubject(studentId, subject).stream()
                .map(ExamUtil::toExamDto)
                .toList();
    }

    public List<ExamDto> getByDateRange(LocalDate startDate, LocalDate endDate){
        return examDao.findByExamDateBetween(startDate, endDate).stream()
                .map(ExamUtil::toExamDto)
                .toList();
    }

    public ExamDto addExam(ExamDto examDto){
        Student student = studentDao.findById(examDto.getStudentId()).orElseThrow(()-> new StudentNotFoundException("Student not found"));
        Exam exam = ExamUtil.toExam(examDto, student);
        student.addExam(exam);
        examDao.save(exam);
        return ExamUtil.toExamDto(exam);
    }

    public void assignExamToAllStudents(ExamCreationRequest request){
        List<Student> students = studentDao.findAll();

        List<Exam> exams = students.stream()
                .map(student -> {
                    Exam exam = new Exam();
                    exam.setStudent(student);
                    exam.setExamName(request.examName());
                    exam.setSubject(request.subject());
                    exam.setExamDate(request.examDate());
                    exam.setScore(Double.valueOf(0));
                    return exam;
                }).toList();

        examDao.saveAll(exams);
    }
}
