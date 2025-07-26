package org.example.athenabackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.athenabackend.dtoSummaries.StudentSummaryDto;
import org.example.athenabackend.model.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TeacherDto {
    private Integer id;
    private String username;
    private String password;
    private String displayName;
    private String nrcNumber;
    private String qualification;
    private LocalDate dob;
    private String phoneNumber;
    private String address;
    private BigDecimal earning;
    private Gender gender;
    private String profileImagePath;
    private List<StudentSummaryDto> students;
    private List<String> subjects;
}
