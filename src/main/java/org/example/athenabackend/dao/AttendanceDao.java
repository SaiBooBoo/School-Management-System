package org.example.athenabackend.dao;

import org.example.athenabackend.entity.Attendance;
import org.example.athenabackend.model.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface AttendanceDao extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByStudentId(Integer studentId);

    List<Attendance> findByStatus(AttendanceStatus status);

    List<Attendance> findByStatusAndDateBetween(AttendanceStatus status, LocalDate startDate, LocalDate endDate);
}

