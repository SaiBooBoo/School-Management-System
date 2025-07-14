package org.example.athenabackend.dtoSummaries;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.athenabackend.model.Subject;

@Data
@AllArgsConstructor
public class TeacherSummaryDto {
    private Integer id;
    private String displayName;
    private String qualification;
    private String phone;
    private Subject subject;
    private String profileImagePath;
}
