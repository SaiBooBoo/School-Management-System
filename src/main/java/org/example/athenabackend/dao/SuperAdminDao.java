package org.example.athenabackend.dao;

import org.example.athenabackend.entity.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperAdminDao extends JpaRepository<SuperAdmin, Integer> {
}
