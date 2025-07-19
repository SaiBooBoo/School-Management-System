package org.example.athenabackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.entity.Subject;
import org.example.athenabackend.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<Subject> createSubject(@RequestParam String subjectName){
        Subject created = subjectService.createSubject(subjectName);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable Integer id){
        subjectService.deleteSubject(id);
        return ResponseEntity.ok("Deleted subject with id " + id);
    }

    @GetMapping
    public ResponseEntity<List<Subject>> listAllSubjects(){
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }
}
