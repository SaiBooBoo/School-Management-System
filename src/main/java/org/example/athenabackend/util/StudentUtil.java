package org.example.athenabackend.util;

import org.example.athenabackend.dto.ParentOrGuardianDto;
import org.example.athenabackend.dto.StudentDto;
import org.example.athenabackend.dto.TeacherDto;
import org.example.athenabackend.dtoSummaries.ParentSummaryDto;
import org.example.athenabackend.dtoSummaries.StudentSummaryDto;
import org.example.athenabackend.entity.ParentOrGuardian;
import org.example.athenabackend.entity.Student;
import org.example.athenabackend.entity.Teacher;
import org.springframework.beans.BeanUtils;

import java.util.List;

import static org.example.athenabackend.util.ParentUtil.toParentSummaryDto;

public class StudentUtil {



    public static StudentDto toStudentDto(Student s) {
        List<ParentSummaryDto> parents = s.getParents().stream()
                .map(link -> toParentSummaryDto(link.getParentOrGuardian()))
                .toList();

        return new StudentDto(
                s.getId(),
                s.getUsername(),
                s.getPassword(),
                s.getDisplayName(),
                s.getGender(),
                s.getDob(),
                s.getAddress(),
                s.getGrade(),
                s.getProfileImagePath(),
                parents
        );
    }

    public static Student toStudent(StudentDto studentDto) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDto, student);
        return student;
    }

    public static StudentSummaryDto toStudentSummaryDto(Student s) {
        return new StudentSummaryDto(
                s.getId(),
                s.getUsername(),
                s.getDisplayName(),
                s.getDob(),
                s.getGrade(),
                s.getProfileImagePath()
        );
    }
}
