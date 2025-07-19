package org.example.athenabackend.dtoSummaries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSummaryDto {
    private Integer id;
    private String username;
    private String displayName;
    private LocalDate dob;
    private BigDecimal grade;
    private String profileImagePath;
    private String address;
}

