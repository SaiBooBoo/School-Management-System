package org.example.athenabackend.dtoSummaries;

import lombok.*;
import org.example.athenabackend.model.ParentType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParentSummaryDto {
    private Integer id;
    private String displayName;
    private String job;
    private String phoneNumber;
    private ParentType parentType;
    private String profileImagePath;
}