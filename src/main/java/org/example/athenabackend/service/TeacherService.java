package org.example.athenabackend.service;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dao.StudentDao;
import org.example.athenabackend.dao.SubjectDao;
import org.example.athenabackend.dao.TeacherDao;
import org.example.athenabackend.dto.TeacherDto;
import org.example.athenabackend.dtoSummaries.StudentSummaryDto;
import org.example.athenabackend.dtoSummaries.TeacherSummaryDto;
import org.example.athenabackend.entity.Student;
import org.example.athenabackend.entity.StudentTeacher;
import org.example.athenabackend.entity.Subject;
import org.example.athenabackend.entity.Teacher;
import org.example.athenabackend.exception.*;
import org.example.athenabackend.util.StudentUtil;
import org.example.athenabackend.util.TeacherUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherService {
    private final TeacherDao teacherDao;
    private final StudentDao studentDao;
    private final SubjectDao subjectDao;

    public List<TeacherSummaryDto> getTeachersBySubject (String subjectName){
        return teacherDao.findDistinctBySubjectsSubjectName(subjectName)
                .stream()
                .map(TeacherUtil::toTeacherSummaryDto)
                .toList();
    }

    public List<Subject> getSubjectsByTeacherId(Integer teacherId) {
        List<Subject> teacher = teacherDao.getSubjectsByTeacherId(teacherId);
        return teacher;
    }

    public List<TeacherSummaryDto> getAllTeacherSummaries(){
        return teacherDao.findAllWithSubjects()
                .stream()
                .map(TeacherUtil::toTeacherSummaryDto)
                .collect(Collectors.toList());
    }


    public void addSubjectToTeacher(Integer teacherId, Integer subjectId){
        Teacher teacher = teacherDao.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException(teacherId));
        Subject subject = subjectDao.findById(subjectId).orElseThrow(() -> new SubjectNotFoundException(subjectId));
        if(teacher.getSubjects().contains(subject)){
            throw new SubjectAlreadyExistsException(subject.getSubjectName());
        }
        teacher.getSubjects().add(subject);
        teacherDao.save(teacher);
    }

    public List<TeacherDto> getAllTeachers(){
        return teacherDao.findAll().stream()
                .map(TeacherUtil::toTeacherDto)
                .toList();
    }

    public TeacherDto getTeacherById(Integer id){
        return teacherDao.findById(id)
                .map(TeacherUtil::toTeacherDto)
                .orElseThrow(() -> new TeacherNotFoundException(id));
    }

    public void deleteTeacherById(Integer id){
        teacherDao.deleteById(id);
    }

    public TeacherDto updateTeacher(Integer id, TeacherDto teacherDto){
        Teacher teacher = TeacherUtil.toTeacher(teacherDto);
        teacher.setId(id);
        teacher = teacherDao.saveAndFlush(teacher);
        return TeacherUtil.toTeacherDto(teacher);
    }

    public List<TeacherDto> searchByName(String name) {
        return teacherDao.findByUsernameContainingIgnoreCase(name)
                .stream()
                .map(TeacherUtil::toTeacherDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TeacherDto addStudentToTeacher(Integer id, TeacherDto teacherDto) {
        Teacher teacher = teacherDao.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));

        new ArrayList<>(teacher.getStudents()).forEach(link -> {
            Student s = link.getStudent();
            teacher.getStudents().remove(link);
            s.getTeachers().remove(link);
        });

        for (StudentSummaryDto sd : teacherDto.getStudents()){
            Student student = null;

            if(sd.getId() != null){
                student = studentDao.findById(sd.getId()).orElseThrow(() -> new StudentNotFoundException(id));
            } else if (sd.getUsername() != null){
                student = studentDao.findByUsername(sd.getUsername()).orElseThrow(() -> new StudentNotFoundException("The student's id that you want to add doesn't exist."));
            } else {
                throw new IllegalArgumentException("Must supply id or username for student");
            }

            StudentTeacher ts = new StudentTeacher(teacher, student);
            teacher.addStudent(ts);

        }

        Teacher savedTeacher = teacherDao.saveAndFlush(teacher);
        return TeacherUtil.toTeacherDto(savedTeacher);
    }

    public void removeStudentFromTeacher(Integer teacherId, Integer studentId) {
        Teacher teacher = teacherDao.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException(teacherId));
        Student student = studentDao.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        // Find the StudentTeacher link
        StudentTeacher toRemove = teacher.getStudents().stream()
                .filter(st -> st.getStudent().equals(student) && st.getTeacher().equals(teacher))
                .findFirst()
                .orElseThrow(() -> new StudentDoesNotExistInTeacherException(student.getDisplayName(), teacher.getDisplayName()));

        // Remove the link from both sides
        teacher.getStudents().remove(toRemove);
        student.getTeachers().remove(toRemove);

        teacherDao.saveAndFlush(teacher);
    }

    public void removeSubjectFromTeacher(Integer teacherId, Integer subjectId) {
        Teacher teacher = teacherDao.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException(teacherId));
        Subject subject = subjectDao.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException(subjectId));
        teacher.getSubjects().remove(subject);
        teacherDao.saveAndFlush(teacher);
    }


}
