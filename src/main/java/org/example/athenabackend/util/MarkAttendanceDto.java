package org.example.athenabackend.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.example.athenabackend.model.AttendanceStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MarkAttendanceDto {
    @NotNull
    private LocalDate date;
    @NotNull
    private AttendanceStatus status;
}
