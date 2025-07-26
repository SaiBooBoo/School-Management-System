package org.example.athenabackend.dao;

import org.example.athenabackend.entity.Role;
import org.example.athenabackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    @Query("""
    select u.roles from User u where u.username= :username
""")
    Optional<Role> findRolesByUsername(String username);

    boolean existsByUsername(String username);
}
