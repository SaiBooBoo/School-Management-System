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
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public Integer register(String username,
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
                            String profileImagePath,
                            Subject subject,
                            BigDecimal grade,
                            Role roles) {

        if(userDao.existsByUsername(username)){
            throw new UsernameAlreadyExistException(username);
        }
       return switch(accountType){
            case "teacher" -> {
                Role teacherRole = findRoleByName("ROLE_TEACHER");
                if(!Objects.nonNull(teacherRole)){
                    teacherRole = new Role();
                    teacherRole.setRoleName("ROLE_TEACHER");
                    teacherRole = roleDao.save(teacherRole);
                }
                Teacher teacher = new Teacher(username, passwordEncoder.encode(password),displayName,nrcNumber, qualification, dob, phoneNumber, address, BigDecimal.valueOf(0), profileImagePath, gender);
                teacher.getRoles().add(teacherRole);
                Teacher savedTeacher = teacherDao.save(teacher);

                yield savedTeacher.getId();
            }
            case "student" -> {
                Role studentRole = findRoleByName("ROLE_STUDENT");
                if(!Objects.nonNull(studentRole)){
                    studentRole = new Role();
                    studentRole.setRoleName("ROLE_STUDENT");
                    studentRole = roleDao.save(studentRole);
                }
                Student student = new Student(username, passwordEncoder.encode(password),displayName, gender, dob, address, grade);
                student.getRoles().add(studentRole);
                Student savedStudent = studentDao.save(student);
                yield savedStudent.getId();
            }
            case "parent" -> {
                Role parentOrGuardianRole = findRoleByName("ROLE_PARENT");
                if (!Objects.nonNull(parentOrGuardianRole)) {
                    parentOrGuardianRole = new Role();
                    parentOrGuardianRole.setRoleName("ROLE_PARENT");
                    parentOrGuardianRole = roleDao.save(parentOrGuardianRole);
                }
                ParentOrGuardian parentOrGuardian = new ParentOrGuardian(username, passwordEncoder.encode(password), displayName, gender,nrcNumber, dob, job, phoneNumber, address, ParentType.valueOf(parentType), profileImagePath);
                parentOrGuardian.getRoles().add(parentOrGuardianRole);
                ParentOrGuardian savedParentOrGuardian = parentOrGuardianDao.save(parentOrGuardian);
                yield savedParentOrGuardian.getId();
            }
            default -> throw new IllegalArgumentException("Unsupported account type: " + accountType);
        };

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

    public String findAccountTypeByUsername(String username) {
        String accountType = userDao.findRolesByUsername(username).stream()
                .map(r -> r.getRoleName())
                .collect(Collectors.joining(","));
        return accountType;
    }
}
