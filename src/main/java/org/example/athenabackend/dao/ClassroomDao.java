package org.example.athenabackend.dao;


import org.example.athenabackend.dtoSummaries.StudentSummaryRecord;
import org.example.athenabackend.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClassroomDao extends JpaRepository<Classroom, Integer> {

    @Query("""
        select new org.example.athenabackend.dtoSummaries.StudentSummaryRecord(
            s.id, s.displayName, s.address, s.dob, c.id, c.name)
        from Classroom c join c.students s
        where c.id = :classroomId
    """)
    List<StudentSummaryRecord> findStudentSummariesByClassroomId(@Param("classroomId") Integer classroomId);

    Optional<Classroom> findByName(String name);

}
