package org.example.athenabackend.dao;

import org.example.athenabackend.dtoSummaries.TeacherSummaryDto;
import org.example.athenabackend.entity.Subject;
import org.example.athenabackend.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface TeacherDao extends JpaRepository<Teacher, Integer> {
    List<Teacher> findByUsernameContainingIgnoreCase(String name);

    List<Teacher> findDistinctBySubjectsSubjectName(String subjectName);

    @Query("select t from Teacher t left join fetch t.subjects")
    List<Teacher> findAllWithSubjects();

    @Query("Select t.subjects from Teacher t where t.id = :teacherId")
    List<Subject> getSubjectsByTeacherId(Integer teacherId);
}
