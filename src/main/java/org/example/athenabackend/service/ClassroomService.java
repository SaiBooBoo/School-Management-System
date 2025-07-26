package org.example.athenabackend.service;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dao.ClassroomDao;
import org.example.athenabackend.dao.StudentDao;
import org.example.athenabackend.dao.TeacherDao;
import org.example.athenabackend.dtoSummaries.StudentSummaryRecord;
import org.example.athenabackend.dtoSummaries.TeacherSummaryDto;
import org.example.athenabackend.dtoSummaries.TeacherSummaryRecord;
import org.example.athenabackend.entity.Classroom;
import org.example.athenabackend.entity.Student;
import org.example.athenabackend.entity.Teacher;
import org.example.athenabackend.exception.ClassroomAlreadyExistsException;
import org.example.athenabackend.exception.ClassroomNotFoundException;
import org.example.athenabackend.exception.StudentNotFoundException;
import org.example.athenabackend.exception.TeacherNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassroomService {
    private final ClassroomDao classroomDao;
    private final StudentDao studentDao;
    private final TeacherDao teacherDao;

    public Classroom createClassroom(String name){
       if(classroomDao.findByName(name).isPresent()){
           throw new ClassroomAlreadyExistsException(name);
       }
        Classroom c = new Classroom();
        c.setName(name);
        return classroomDao.save(c);
    }

    public void addStudentToClassroom(Integer classroomId, Integer studentId){
        Classroom c = classroomDao.findById(classroomId).orElseThrow(() -> new ClassroomNotFoundException(classroomId));
        Student s = studentDao.findById(studentId).orElseThrow(() -> new StudentNotFoundException(studentId));
        c.getStudents().add(s);
    }

    public void removeStudentFromClassroom(Integer classroomId, Integer studentId){
        Classroom c = classroomDao.findById(classroomId).orElseThrow(() -> new ClassroomNotFoundException(classroomId));
        Student s = studentDao.findById(studentId).orElseThrow(() -> new StudentNotFoundException(studentId));
        c.getStudents().remove(s);
        classroomDao.save(c);
    }

    public void addTeacherToClassroom(Integer classroomId, Integer teacherId){
        Classroom c = classroomDao.findById(classroomId).orElseThrow(() -> new ClassroomNotFoundException(classroomId));
        Teacher t = teacherDao.findById(teacherId).orElseThrow(() -> new TeacherNotFoundException(teacherId));
        c.getTeachers().add(t);
        classroomDao.save(c);
    }

    public void removeTeacherFromClassroom(Integer classroomId, Integer teacherId){
        Classroom c = classroomDao.findById(classroomId).orElseThrow(() -> new ClassroomNotFoundException(classroomId));
        Teacher t = teacherDao.findById(teacherId).orElseThrow(() -> new TeacherNotFoundException(teacherId));
        c.getTeachers().remove(t);
        classroomDao.save(c);
    }

    @Transactional(readOnly = true)
    public List<StudentSummaryRecord> getStudentsInClassroom(Integer classroomId){
        return classroomDao.findStudentSummariesByClassroomId(classroomId);

    }

    @Transactional(readOnly = true)
    public List<TeacherSummaryRecord> getTeachersInClassroom(Integer classroomId) {
        return classroomDao.findTeacherSummariesByClassroomId(classroomId);
    }
}
