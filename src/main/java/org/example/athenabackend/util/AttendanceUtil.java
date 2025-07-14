package org.example.athenabackend.util;

import org.example.athenabackend.dto.AttendanceDto;
import org.example.athenabackend.entity.Attendance;
import java.util.List;
import static org.example.athenabackend.util.StudentUtil.toStudentSummaryDto;

public class AttendanceUtil {

    public static AttendanceDto toAttendanceDto(Attendance attendance) {
        return new AttendanceDto(
                attendance.getId(),
                attendance.getDate(),
                attendance.getStatus(),
                List.of(toStudentSummaryDto(attendance.getStudent()))
        );
    }
}
