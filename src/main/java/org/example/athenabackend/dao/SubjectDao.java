package org.example.athenabackend.dao;

import org.example.athenabackend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectDao extends JpaRepository<Subject, Integer > {

    Optional<Subject> findBySubjectName(String subjectName);
}
