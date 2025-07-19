package org.example.athenabackend.dtoSummaries;

import java.time.LocalDate;

public record StudentSummaryRecord(
        Integer id,
        String name,
        String address,
        LocalDate dob,
        Integer classroomId,
        String classroomName
) {
}

