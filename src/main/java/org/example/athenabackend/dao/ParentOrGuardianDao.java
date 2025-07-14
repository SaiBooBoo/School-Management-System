package org.example.athenabackend.dao;

import org.example.athenabackend.entity.ParentOrGuardian;
import org.example.athenabackend.model.ParentType;
import org.hibernate.annotations.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ParentOrGuardianDao extends JpaRepository<ParentOrGuardian, Integer> {
    Optional<ParentOrGuardian> findByUsername(String username);

    List<ParentOrGuardian> findByUsernameContainingIgnoreCase(String username);

    List<ParentOrGuardian> findByParentType(ParentType parentType);

    Optional<ParentOrGuardian>  findByDisplayName(String name);
}
