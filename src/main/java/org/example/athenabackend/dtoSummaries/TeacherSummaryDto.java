package org.example.athenabackend.dtoSummaries;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.athenabackend.dto.SubjectDto;
import org.example.athenabackend.model.Gender;


import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherSummaryDto {
    private Integer id;
    private String displayName;
    private String qualification;
    private String phone;
    private LocalDate dob;
    private Gender gender;
    private String nrcNumber;
    private String profileImagePath;
    private List<String> subjects;
}
