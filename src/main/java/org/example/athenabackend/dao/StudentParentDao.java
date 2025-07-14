package org.example.athenabackend.dao;

import org.example.athenabackend.entity.StudentParent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentParentDao extends JpaRepository<StudentParent, Integer> {

    List<StudentParent> findAllByStudentId(Integer studentId);
}
