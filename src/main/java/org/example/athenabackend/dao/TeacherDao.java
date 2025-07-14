package org.example.athenabackend.dao;

import org.example.athenabackend.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TeacherDao extends JpaRepository<Teacher, Integer> {
    List<Teacher> findByUsernameContainingIgnoreCase(String name);
}
