package org.example.athenabackend.util;

import org.example.athenabackend.dto.TeacherDto;
import org.example.athenabackend.dtoSummaries.StudentSummaryDto;
import org.example.athenabackend.dtoSummaries.TeacherSummaryDto;
import org.example.athenabackend.entity.Teacher;
import org.springframework.beans.BeanUtils;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class TeacherUtil {

    public static TeacherDto toTeacherDto(Teacher teacher) {
        List<TeacherSummaryDto> teachers = teacher.getStudents().stream()
                .map(link -> toTeacherSummaryDto(link.getTeacher()))
                .toList();
        List<StudentSummaryDto> students = teacher.getStudents().stream()
                .map(link -> StudentUtil.toStudentSummaryDto(link.getStudent()))
                .toList();
        TeacherDto teacherDto = new TeacherDto(
                teacher.getId(),
                teacher.getUsername(),
                teacher.getPassword(),
                teacher.getDisplayName(),
                teacher.getNrcNumber(),
                teacher.getQualification(),
                teacher.getDob(),
                teacher.getPhone(),
                teacher.getAddress(),
                teacher.getEarning(),
                teacher.getSubject(),
                teacher.getProfileImagePath(),
                students
        );
        BeanUtils.copyProperties(teacher, teacherDto);
        return teacherDto;
    }

    public static Teacher toTeacher(TeacherDto teacherDto) {
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherDto, teacher);
        return teacher;
    }
    public static TeacherSummaryDto toTeacherSummaryDto(Teacher teacher){
        return new TeacherSummaryDto(
                teacher.getId(),
                teacher.getDisplayName(),
                teacher.getQualification(),
                teacher.getPhone(),
                teacher.getSubject(),
                teacher.getProfileImagePath()
        );
    }
}
