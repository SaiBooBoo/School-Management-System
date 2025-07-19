package org.example.athenabackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dao.TeacherDao;
import org.example.athenabackend.dto.TeacherDto;
import org.example.athenabackend.dtoSummaries.TeacherSummaryDto;
import org.example.athenabackend.entity.Teacher;
import org.example.athenabackend.exception.TeacherImageNotFoundException;
import org.example.athenabackend.exception.TeacherNotFoundException;
import org.example.athenabackend.service.FileStorageService;
import org.example.athenabackend.service.TeacherService;
import org.example.athenabackend.util.TeacherUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final FileStorageService fileStorageService;
    private final TeacherDao teacherDao;

    @DeleteMapping("/{teacherId}/subjects/{subjectId}")
    public ResponseEntity<String> removeSubjectFromTeacher(@PathVariable Integer teacherId,
                                                           @PathVariable Integer subjectId){
        teacherService.removeSubjectFromTeacher(teacherId, subjectId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Subject with id " + subjectId + " has been removed from teacher with id " + teacherId);
    }

    @GetMapping("/by-subject")
    public ResponseEntity<List<TeacherSummaryDto>> getTeachersBySubject(@RequestParam String subjectName){
        return ResponseEntity.ok(teacherService.getTeachersBySubject(subjectName));
    }

    @PostMapping("/{teacherId}/subjects/{subjectId}")
    public ResponseEntity<Void> addSubjectToTeacher(@PathVariable Integer teacherId,
                                                    @PathVariable Integer subjectId){
        teacherService.addSubjectToTeacher(teacherId, subjectId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public List<TeacherDto> searchTeacherByName(@RequestParam("username") String username){
        return teacherService.searchByName(username);
    }

    @GetMapping
    public List<TeacherDto> getAllTeachers(){
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getTeacherImage(@PathVariable Integer id){
        Teacher t = teacherDao.findById(id).orElseThrow(() -> new TeacherNotFoundException(id));

        String fileName = t.getProfileImagePath();
        if(fileName == null){
            throw new TeacherImageNotFoundException(id);
        }

        Resource resource = fileStorageService.loadFile(fileName);

        String contentType;
        try{
            Path path = fileStorageService.getFileStorageLocation().resolve(fileName).normalize();
            contentType = Files.probeContentType(path);
            if(contentType ==null){
                contentType = "application/octet-stream";
            }
        } catch (IOException ex) {
            contentType = "application/octet-stream";
        }

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
            .contentType(MediaType.parseMediaType(contentType))
            .body(resource);
    }

    @PutMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadTeacherImage(@PathVariable Integer id,
                                                     @RequestParam("file")MultipartFile file){

        Teacher t = teacherDao.findById(id).orElseThrow(() -> new TeacherNotFoundException(id));

        String oldFile = t.getProfileImagePath();
        if(oldFile != null && !oldFile.isBlank()){
            fileStorageService.deleteFile(oldFile);
        }

        String newFileName = fileStorageService.storeFile(file);

        t.setProfileImagePath(newFileName);
        teacherDao.save(t);

        return ResponseEntity.ok("Teacher image uploaded: " + newFileName);
    }

    @PutMapping("/{id}")
    public TeacherDto updateTeacher(@PathVariable("id") Integer id, @RequestBody TeacherDto teacherDto){
        return teacherService.updateTeacher(id, teacherDto);
    }

    @PutMapping("/addStudents/{id}")
    public TeacherDto addStudentToTeacher(@PathVariable("id") Integer id, @RequestBody TeacherDto teacherDto){
        return teacherService.addStudentToTeacher(id, teacherDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTeacherById(@PathVariable("id") Integer id) {
        teacherService.deleteTeacherById(id);
        return ResponseEntity.ok("Teacher with id " + id + " has been deleted successfully.");
    }

    @DeleteMapping("/{teacherId}/removeStudent/{studentId}")
    public ResponseEntity<String> deleteStudentFromTeacher(
            @PathVariable("teacherId") Integer teacherId,
            @PathVariable("studentId") Integer studentId){
        teacherService.removeStudentFromTeacher(teacherId, studentId);
        return ResponseEntity.ok("Student with id " + studentId + " has been removed from teacher with id " + teacherId);
    }

}
