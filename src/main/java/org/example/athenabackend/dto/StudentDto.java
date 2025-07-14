package org.example.athenabackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.athenabackend.dtoSummaries.ParentSummaryDto;
import org.example.athenabackend.model.Gender;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StudentDto {
    private Integer id;
    private String username;
    private String password;
    private String displayName;
    private Gender gender;
    private LocalDate dob;
    private String address;
    private BigDecimal grade;
    private String profileImage;
    private List<ParentSummaryDto> parents;

}
