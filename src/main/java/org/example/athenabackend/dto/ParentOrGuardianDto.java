package org.example.athenabackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.athenabackend.dtoSummaries.StudentSummaryDto;
import org.example.athenabackend.model.Gender;
import org.example.athenabackend.model.ParentType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ParentOrGuardianDto {
    private Integer id;
    private String username;
    private String password;
    private String displayName;
    private Gender gender;
    private String nrcNumber;
    private LocalDate dob;
    private String job;
    private String phoneNumber;
    private String address;
    private String profileImagePath;
    private ParentType parentType;
    private List<StudentSummaryDto> students;
}
