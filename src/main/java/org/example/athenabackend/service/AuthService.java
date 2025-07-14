package org.example.athenabackend.service;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dao.*;
import org.example.athenabackend.entity.ParentOrGuardian;
import org.example.athenabackend.entity.Role;
import org.example.athenabackend.entity.Student;
import org.example.athenabackend.entity.Teacher;
import org.example.athenabackend.exception.UsernameAlreadyExistException;
import org.example.athenabackend.model.Gender;
import org.example.athenabackend.model.ParentType;
import org.example.athenabackend.model.Subject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RoleDao roleDao;
    private final StudentDao studentDao;
    private final TeacherDao teacherDao;
    private final ParentOrGuardianDao parentOrGuardianDao;
    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final AuthenticationManager authenticationManager;

    public String register(String username,
                           String password,
                           String nrcNumber,
                           String phoneNumber,
                           LocalDate dob,
                           String displayName,
                           Gender gender,
                           String address,
                           String job,
                           String parentType,
                           String qualification,
                           String accountType,
                           String image_url,
                           Subject subject) {
        if(userDao.existsByUsername(username)){
            throw new UsernameAlreadyExistException(username);
        }
        String msg = switch(accountType){
            case "teacher" -> {
                Role teacherRole = findRoleByName("ROLE_TEACHER");
                if(!Objects.nonNull(teacherRole)){
                    teacherRole = new Role();
                    teacherRole.setRoleName("ROLE_TEACHER");
                    teacherRole = roleDao.save(teacherRole);
                }
                Teacher teacher = new Teacher(username, passwordEncoder.encode(password),displayName,nrcNumber, qualification, dob, phoneNumber, address, BigDecimal.valueOf(0), image_url, subject);
                teacher.getRoles().add(teacherRole);
                Teacher savedTeacher = teacherDao.save(teacher);

                yield "Teacher %s successfully registered.".formatted(savedTeacher.getUsername());
            }
            case "student" -> {
                Role studentRole = findRoleByName("ROLE_STUDENT");
                if(!Objects.nonNull(studentRole)){
                    studentRole = new Role();
                    studentRole.setRoleName("ROLE_STUDENT");
                    studentRole = roleDao.save(studentRole);
                }
                Student student = new Student(username, passwordEncoder.encode(password),displayName, gender, dob, address, BigDecimal.valueOf(0));
                student.getRoles().add(studentRole);
                Student savedStudent = studentDao.save(student);
                yield "Student %s successfully registered.".formatted(savedStudent.getUsername());
            }
            case "parent" -> {
                Role parentOrGuardianRole = findRoleByName("ROLE_PARENT");
                if (!Objects.nonNull(parentOrGuardianRole)) {
                    parentOrGuardianRole = new Role();
                    parentOrGuardianRole.setRoleName("ROLE_PARENT");
                    parentOrGuardianRole = roleDao.save(parentOrGuardianRole);
                }
                ParentOrGuardian parentOrGuardian = new ParentOrGuardian(username, passwordEncoder.encode(password), displayName, gender,nrcNumber, dob, job, phoneNumber, address, ParentType.valueOf(parentType), image_url);
                parentOrGuardian.getRoles().add(parentOrGuardianRole);
                ParentOrGuardian savedParentOrGuardian = parentOrGuardianDao.save(parentOrGuardian);
                yield "Parent/Guardian %s successfully registered.".formatted(savedParentOrGuardian.getUsername());
            }
            default -> "Invalid Account Type";
        };
        return msg;
    }

    public Role findRoleByName(String roleName) {
        return roleDao.findByRoleName(roleName).orElse(null);
    }

    public String login(String username, String password) {
        var auth = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(auth);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        StringBuilder sb = new StringBuilder();
        for(var role:authentication.getAuthorities()) {
            sb.append(role);
        }
        return sb.toString();
    }
}
