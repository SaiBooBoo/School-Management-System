package org.example.athenabackend.dtoSummaries;

import lombok.*;
import org.example.athenabackend.model.ParentType;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParentSummaryDto {
    private Integer id;
    private String displayName;
    private String job;
    private String phoneNumber;
    private String nrcNumber;
    private String address;
    private LocalDate dob;
    private ParentType parentType;
    private String profileImagePath;
}