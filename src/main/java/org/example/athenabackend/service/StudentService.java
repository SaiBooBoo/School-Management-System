package org.example.athenabackend.service;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dao.AttendanceDao;
import org.example.athenabackend.dao.ParentOrGuardianDao;
import org.example.athenabackend.dao.StudentDao;


import org.example.athenabackend.dto.AttendanceDto;
import org.example.athenabackend.dto.StudentDto;
import org.example.athenabackend.dtoSummaries.ParentSummaryDto;
import org.example.athenabackend.dtoSummaries.StudentSummaryDto;
import org.example.athenabackend.entity.Attendance;
import org.example.athenabackend.entity.ParentOrGuardian;
import org.example.athenabackend.entity.Student;
import org.example.athenabackend.entity.StudentParent;
import org.example.athenabackend.exception.*;
import org.example.athenabackend.model.AttendanceStatus;
import org.example.athenabackend.util.AttendanceUtil;
import org.example.athenabackend.util.MarkAttendanceDto;
import org.example.athenabackend.util.StudentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {
    private final StudentDao studentDao;

    private final FileStorageService fileStorageService;
    private final ParentOrGuardianDao parentOrGuardianDao;
    private final AttendanceDao attendanceDao;

    public List<AttendanceDto> getAllAttendances() {
        return attendanceDao.findAll()
                .stream()
                .map(AttendanceUtil::toAttendanceDto)
                .toList();
    }

    public List<AttendanceDto> getAttendanceByStudentId(Integer studentId) {
        return attendanceDao.findByStudentId(studentId)
                .stream()
                .map(AttendanceUtil::toAttendanceDto)
                .toList();
    }


    public List<AttendanceDto> getAttendanceByStatus(AttendanceStatus status){
        return attendanceDao.findByStatus(status).stream()
                .map(AttendanceUtil::toAttendanceDto)
                .toList();
    }

    public List<AttendanceDto> getByStatusAndDateRange(AttendanceStatus status, LocalDate startDate, LocalDate endDate){
        return attendanceDao.findByStatusAndDateBetween(status, startDate, endDate)
                .stream()
                .map(AttendanceUtil::toAttendanceDto)
                .toList();
    }

    public List<StudentDto> getAllStudents() {
        return studentDao.findAll().stream()
                .map(StudentUtil::toStudentDto)
                .toList();
    }

    public StudentDto getStudentById(Integer id) {
        return studentDao.findById(id)
                .map(StudentUtil::toStudentDto)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public StudentDto getByStudentName(String studentName) {
        return studentDao.findByUsername(studentName)
                .map(StudentUtil::toStudentDto)
                .orElseThrow(() -> new StudentNotFoundException(studentName));
    }

    public StudentSummaryDto getStudentSummary(Integer studentId) {
        Student student = studentDao.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentId));

        return new StudentSummaryDto(
                student.getId(),
                student.getUsername(),
                student.getDisplayName(),
                student.getDob(),
                student.getGrade(),
                student.getProfileImagePath()
        );
    }

    public StudentDto findStudentWithParents(int studentId) {
        Student s = studentDao.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        return StudentUtil.toStudentDto(s);
    }

    public List<StudentDto> searchByName(String usernamePart) {
        return studentDao.findByUsernameContainingIgnoreCase(usernamePart).stream()
                .map(StudentUtil::toStudentDto)
                .collect(Collectors.toList());
    }

    public AttendanceDto updateAttendance(
            Integer studentId,
            Integer attendanceId,
            MarkAttendanceDto dto
    ){
        studentDao.findById(studentId).orElseThrow(() -> new StudentNotFoundException("Student not found"));

        Attendance record = attendanceDao.findById(attendanceId).orElseThrow(() -> new AttendanceNotFoundException("Attendance not found"));
        if(!record.getStudent().getId().equals(studentId)){
            throw new AttendanceNotBelongToStudentException(studentId);
        }

        record.setDate(dto.getDate());
        record.setStatus(dto.getStatus());

        Attendance saved = attendanceDao.save(record);
        return new AttendanceDto(
                saved.getId(),
                saved.getDate(),
                saved.getStatus(),
                List.of(StudentUtil.toStudentSummaryDto(saved.getStudent()))
        );
    }

    public void markAttendance(Integer studentId, LocalDate date, AttendanceStatus status){
        Student student = studentDao.findById(studentId).orElseThrow(() -> new StudentNotFoundException("Student not found"));
        student.addAttendance(date, status);
        studentDao.save(student);
    }

    public StudentDto addStudent(Student student) {
        Student savedStudent = studentDao.save(student);
        return StudentUtil.toStudentDto(savedStudent);
    }


    public StudentDto updateStudent(Integer id, StudentDto studentDto) {
        Student student = StudentUtil.toStudent(studentDto);
        student.setId(studentDto.getId());
        student = studentDao.saveAndFlush(student);
        return StudentUtil.toStudentDto(student);
    }

    public StudentDto createStudent(StudentDto studentDto, MultipartFile profileImage) {

        // Check if username already exists
        if (studentDao.existsByUsername(studentDto.getUsername())) {
            throw new IllegalArgumentException("username already exists");
        }

        // Handle file upload
        String profileImagePath = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            profileImagePath = fileStorageService.storeFile(profileImage);

            // Create and save student
            Student student = new Student();
            student.setUsername(studentDto.getUsername());
            student.setPassword(studentDto.getPassword());
            student.setDisplayName(studentDto.getDisplayName());
            student.setDob(studentDto.getDob());
            student.setAddress(studentDto.getAddress());
            student.setGrade(studentDto.getGrade());
            student.setProfileImagePath(profileImagePath);
            Student savedStudent = studentDao.save(student);

            return StudentUtil.toStudentDto(savedStudent);
        }
        return studentDto;
    }

        public StudentDto addParentToStudent (Integer studentId, StudentDto studentDto){
            // 1) Load the student
            Student student = studentDao.findById(studentId)
                    .orElseThrow(() -> new StudentNotFoundException(studentId));

            // 2) Remove old links
            new ArrayList<>(student.getParents()).forEach(link -> {
                ParentOrGuardian p = link.getParentOrGuardian();
                student.getParents().remove(link);
                p.getStudents().remove(link);
            });

            // 3) Add new links
            for (ParentSummaryDto pd : studentDto.getParents()) {
                ParentOrGuardian parent = null;

                // fetch existing parent
                if (pd.getId() != null) {
                    parent = parentOrGuardianDao.findById(pd.getId())
                            .orElseThrow(() -> new ParentOrGuardianNotFoundException(pd.getId()));
                } else if (pd.getDisplayName() != null) {
                    parent = parentOrGuardianDao.findByUsername(pd.getDisplayName())
                            .orElseThrow(() -> new ParentOrGuardianNotFoundException(pd.getDisplayName()));
                } else {
                    throw new IllegalArgumentException("Must supply id or username for parent");
                }

                // **Here** build the join‐entity
                StudentParent sp = new StudentParent(parent, student);

                // attach to both sides
                student.addParent(sp);

            }

            // 4) Persist
            Student saved = studentDao.saveAndFlush(student);

            // 5) Map to DTO
            return StudentUtil.toStudentDto(saved);
        }

    public void removeParentFromStudent (Integer studentId, Integer parentId){
        Student student = studentDao.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        ParentOrGuardian parent = parentOrGuardianDao.findById(parentId)
                .orElseThrow(() -> new ParentOrGuardianNotFoundException(parentId));

        StudentParent toRemove = student.getParents().stream()
                .filter(sp -> sp.getParentOrGuardian().equals(parent) && sp.getStudent().equals(student))
                .findFirst()
                .orElseThrow(() -> new ParentAlreadyRemovedException(parentId));

        student.getParents().remove(toRemove);
        parent.getStudents().remove(toRemove);
        studentDao.saveAndFlush(student);
    }

    public void deleteAttendance(Integer studentId, Integer attendanceId){
        Attendance record = attendanceDao.findById(attendanceId).orElseThrow(() -> new AttendanceNotFoundException("Attendance not found"));
        if(!record.getStudent().getId().equals(studentId)){
            throw new AttendanceNotBelongToStudentException(studentId);
        }
        attendanceDao.deleteById(attendanceId);
    }

    public void deleteStudentById(Integer id) {
        studentDao.deleteById(id);
    }
}
