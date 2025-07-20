package org.example.athenabackend.util;

import org.example.athenabackend.dto.ParentOrGuardianDto;
import org.example.athenabackend.dto.StudentDto;
import org.example.athenabackend.dtoSummaries.ParentSummaryDto;
import org.example.athenabackend.dtoSummaries.StudentSummaryDto;
import org.example.athenabackend.entity.ParentOrGuardian;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.athenabackend.util.StudentUtil.toStudentDto;
import static org.example.athenabackend.util.StudentUtil.toStudentSummaryDto;

public class ParentUtil {
    public static ParentOrGuardianDto toParentOrGuardianDto(ParentOrGuardian p) {
        List<StudentSummaryDto> students = p.getStudents().stream()
                .map(link -> toStudentSummaryDto(link.getStudent()))
                .toList();

        return new ParentOrGuardianDto(
                p.getId(),
                p.getUsername(),
                p.getPassword(),
                p.getDisplayName(),
                p.getGender(),
                p.getNrcNumber(),
                p.getDob(),
                p.getJob(),
                p.getPhoneNumber(),
                p.getAddress(),
                p.getProfileImagePath(),
                p.getParentType(),
                students
        );
    }


    public static ParentOrGuardian toParentOrGuardian(ParentOrGuardianDto parentOrGuardianDto) {
        ParentOrGuardian parentOrGuardian = new ParentOrGuardian();
        BeanUtils.copyProperties(parentOrGuardianDto, parentOrGuardian);
        return parentOrGuardian;
    }

    public static ParentSummaryDto toParentSummaryDto(ParentOrGuardian p) {
        return new ParentSummaryDto(
                p.getId(),
                p.getDisplayName(),
                p.getJob(),
                p.getPhoneNumber(),
                p.getNrcNumber(),
                p.getAddress(),
                p.getDob(),
                p.getParentType(),
                p.getProfileImagePath()
        );
    }
}
