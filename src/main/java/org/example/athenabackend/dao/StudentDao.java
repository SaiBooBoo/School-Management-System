package org.example.athenabackend.dao;

import org.example.athenabackend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentDao extends JpaRepository<Student, Integer> {

      Optional<Student> findByUsername(String username);

      List<Student> findByUsernameContainingIgnoreCase(String username);

      boolean existsByUsername(String username);


}
