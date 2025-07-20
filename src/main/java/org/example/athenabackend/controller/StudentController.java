package org.example.athenabackend.controller;

import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dao.StudentDao;
import org.example.athenabackend.dto.AttendanceDto;
import org.example.athenabackend.dto.StudentDto;
import org.example.athenabackend.entity.Student;
import org.example.athenabackend.exception.StudentNotFoundException;
import org.example.athenabackend.exception.StudentProfileImageNotFoundException;
import org.example.athenabackend.model.AttendanceStatus;
import org.example.athenabackend.service.FileStorageService;
import org.example.athenabackend.service.StudentService;
import org.example.athenabackend.util.MarkAttendanceDto;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;
    private final FileStorageService fileStorageService;
    private final StudentDao studentDao;

    @GetMapping("{id}/image")
    public ResponseEntity<Resource> getProfileImageByStudentId(@PathVariable Integer id) {
        Student s = studentDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        String fileName = s.getProfileImagePath();
        Resource resource = fileStorageService.loadFile(fileName);

        String contentType;
        try{
            Path filePath = fileStorageService.getFileStorageLocation().resolve(fileName).normalize();
            contentType = Files.probeContentType(filePath);
            if(contentType ==null){
                contentType = "application/octet-stream";
            }
        }
        catch(IOException ex) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable("filename") String fileName){
        Resource resource = fileStorageService.loadFile(fileName);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }

    @GetMapping("/{id}/profile-image")
    public ResponseEntity<Resource>  getProfileImage(@PathVariable Integer id){
        Student student = studentDao.findById(id).orElseThrow(() -> new StudentNotFoundException(id));

        if(student.getProfileImagePath() == null){
            throw new StudentProfileImageNotFoundException(id);
        }

        Resource file = fileStorageService.loadFile(student.getProfileImagePath());
        String contentType;
        try{
            contentType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException ex){
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }

    @GetMapping("/search")
    public List<StudentDto> searchByName(@RequestParam("username") String username){
        return studentService.searchByName(username);
    }

    @GetMapping
    public List<StudentDto> getStudents() {
        return studentService.getAllStudents();
    }

    // @GetMapping("/{id}")
    public StudentDto getStudentById(@PathVariable("id") Integer id){
        return studentService.getStudentById(id);
    }

    @GetMapping("/{id}")
   public StudentDto getById(@PathVariable int id){
       return studentService.findStudentWithParents(id);
   }

   @GetMapping("/attendances")
   public List<AttendanceDto> getAllAttendance(){
        return studentService.getAllAttendances();
   }

   @GetMapping("/{studentId}/attendances")
   public List<AttendanceDto> getAttendanceByStudentId(@PathVariable Integer studentId){
        return studentService.getAttendanceByStudentId(studentId);
   }

   @GetMapping("/attendances/status/{status}")
   public List<AttendanceDto> getByStatus(@PathVariable AttendanceStatus status){
        return studentService.getAttendanceByStatus(status);
   }

    @GetMapping("/attendances/filter")
    public List<AttendanceDto> getByStatusAndDate(
            @RequestParam AttendanceStatus status,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ){
        return studentService.getByStatusAndDateRange(status, startDate, endDate);
    }

    @PutMapping("/{id}/update-image")
    public ResponseEntity<String> updateProfileImage(@PathVariable Integer id, @RequestParam("file") MultipartFile file){

        Student student = studentDao.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        String oldFile = student.getProfileImagePath();
        if(oldFile != null && !oldFile.isBlank()){
            fileStorageService.deleteFile(oldFile);
        }

        String newFileName = fileStorageService.storeFile(file);

        student.setProfileImagePath(newFileName);
        studentDao.save(student);

        return ResponseEntity.ok("Student image uploaded: " + newFileName);
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadProfileImage(@PathVariable Integer id, @RequestParam("file") MultipartFile file){


        String path = fileStorageService.storeFile(file);
        return ResponseEntity.ok("Image uploaded successfully: " +path);
    }

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(
            @RequestBody StudentDto studentDto,
            @RequestParam(value = "profileImage", required = false)MultipartFile profileImage){
        try{
            StudentDto createStudent = studentService.createStudent(studentDto, profileImage);
            return ResponseEntity.status(HttpStatus.CREATED).body(createStudent);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(studentDto);
        }
    }

    @PostMapping("/{id}/attendance")
    public ResponseEntity<Void> mark(@PathVariable Integer id, @RequestBody MarkAttendanceDto dto){
        studentService.markAttendance(id, dto.getDate(), dto.getStatus());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public StudentDto updateStudent(@PathVariable("id") Integer id,@RequestBody StudentDto studentDto){
        return studentService.updateStudent(id,studentDto);
    }

    @PutMapping("/addParents/{id}")
    public StudentDto addParentToStudent(@PathVariable("id") Integer id, @RequestBody StudentDto studentDto){
        return studentService.addParentToStudent(id,studentDto);
    }

    @PutMapping("/{studentId}/attendance/{attendanceId}")
    public AttendanceDto updateAttendance(
            @PathVariable("studentId") Integer studentId,
            @PathVariable("attendanceId") Integer attendanceId,
            @RequestBody @Validated MarkAttendanceDto dto){
        return studentService.updateAttendance(studentId, attendanceId, dto);
    }

    @DeleteMapping("/{studentId}/attendance/{attendanceId}")
    public ResponseEntity<Void> delete(@PathVariable Integer studentId, @PathVariable Integer attendanceId){
        studentService.deleteAttendance(studentId, attendanceId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/removeParent/{studentId}/{parentId}")
    public ResponseEntity<String> removeParentFromStudent(
            @PathVariable Integer studentId,
            @PathVariable Integer parentId){
        studentService.removeParentFromStudent(studentId, parentId);
        return ResponseEntity.ok().body("Parent with id " + parentId + " has been removed from student with id " + studentId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable Integer id){
        studentService.deleteStudentById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
