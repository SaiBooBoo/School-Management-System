package org.example.athenabackend.dao;

import org.example.athenabackend.entity.StudentTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentTeacherDao extends JpaRepository<StudentTeacher, Integer> {
}
