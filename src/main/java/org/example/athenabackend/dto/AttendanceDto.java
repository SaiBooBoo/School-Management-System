package org.example.athenabackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.athenabackend.dtoSummaries.StudentSummaryDto;
import org.example.athenabackend.model.AttendanceStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDto {
    private Integer id;
    private LocalDate date;
    private AttendanceStatus status;
    private List<StudentSummaryDto> student;
}
