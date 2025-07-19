package org.example.athenabackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dtoSummaries.StudentSummaryRecord;
import org.example.athenabackend.entity.Classroom;
import org.example.athenabackend.entity.Student;
import org.example.athenabackend.service.ClassroomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classroom")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomService classroomService;

    @PostMapping
    public ResponseEntity<Classroom> createClassroom(@RequestParam String name){
        Classroom created = classroomService.createClassroom(name);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{classroomId}/addStudent/{studentId}")
    public ResponseEntity<String> addStudentToClassroom(@PathVariable Integer classroomId, @PathVariable Integer studentId){
        classroomService.addStudentToClassroom(classroomId, studentId);
        return ResponseEntity.ok("Student with id " + studentId + " has been added to classroom with id " + classroomId);
    }

    @DeleteMapping("/{classroomId}/removeStudent/{studentId}")
    public ResponseEntity<String> removeStudentFromClassroom(@PathVariable Integer classroomId, @PathVariable Integer studentId){
        classroomService.removeStudentFromClassroom(classroomId, studentId);
        return ResponseEntity.ok("Student with id " + studentId + " has been removed from classroom with id " + classroomId);
    }

    @PutMapping("/{classroomId}/addTeacher/{teacherId}")
    public ResponseEntity<String> addTeacherToClassroom(@PathVariable Integer classroomId, @PathVariable Integer teacherId){
        classroomService.addTeacherToClassroom(classroomId, teacherId);
        return ResponseEntity.ok("Teacher with id " + teacherId + " has been added to classroom with id " + classroomId);
    }

    @DeleteMapping("/{classroomId}/removeTeacher/{teacherId}")
    public ResponseEntity<String> removeTeacherFromClassroom(@PathVariable Integer classroomId, @PathVariable Integer teacherId){
        classroomService.removeTeacherFromClassroom(classroomId, teacherId);
        return ResponseEntity.ok("Teacher with id " + teacherId + " has been removed from classroom with id " + classroomId);
    }

    @GetMapping("/{classroomId}/students")
    public ResponseEntity<List<StudentSummaryRecord>> getStudentsInClassroom(@PathVariable Integer classroomId){
        List<StudentSummaryRecord> students = classroomService.getStudentsInClassroom(classroomId);
        return ResponseEntity.ok(students);
    }
}
