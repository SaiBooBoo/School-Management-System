package org.example.athenabackend;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dao.*;
import org.example.athenabackend.entity.*;
import org.example.athenabackend.exception.ParentOrGuardianNotFoundException;
import org.example.athenabackend.exception.StudentNotFoundException;
import org.example.athenabackend.model.Gender;
import org.example.athenabackend.model.ParentType;
import org.example.athenabackend.model.Subject;
import org.example.athenabackend.service.AuthService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
@RequiredArgsConstructor
public class AthenaBackendApplication {
    private final StudentDao studentDao;
    private final TeacherDao teacherDao;
    private final SuperAdminDao superAdminDao;
    private final ParentOrGuardianDao parentOrGuardianDao;
    private final StudentParentDao studentParentDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthService  authService;
    private final FeeDao feeDao;

    @Bean
    public ApplicationRunner runner1(){
        return r -> {
            teacherDao.findById(19).ifPresent(t -> System.out.println(t.getDisplayName()));
        };
    }

    @Bean @Transactional @Profile("dev")
    public ApplicationRunner runner() {
        return r -> {
            Role studentRole = new Role();
            studentRole.setRoleName("ROLE_STUDENT");
            Role parentOrGuardianRole = new Role();
            parentOrGuardianRole.setRoleName("ROLE_PARENT_OR_GUARDIAN");
            Role teacherRole = new Role();
            teacherRole.setRoleName("ROLE_TEACHER");
            Role superAdminRole = new Role();
            superAdminRole.setRoleName("ROLE_SUPER_ADMIN");

//            Student student = new Student("susan", passwordEncoder.encode("12345"),"susan", Gender.FEMALE ,LocalDate.of(2002,4,6), "Muse", BigDecimal.valueOf(6));
//            student.getRoles().add(studentRole);

            ParentOrGuardian parentOrGuardian = new ParentOrGuardian("Kushina Uzumaki", passwordEncoder.encode("12345"), "Kushina",Gender.FEMALE, "12/ABC(N)123456", LocalDate.of(2000, 5, 3), "BAs English", "09443289890", "Leaf Village", ParentType.MOTHER, "url");
            parentOrGuardian.getRoles().add(teacherRole);

            Teacher teacher = new Teacher("jane", passwordEncoder.encode("12345"),"Jane","12/ABC(N)123456", "BAs English", LocalDate.of(2000, 5, 3),"09443289890", "Gone Naung Mo", BigDecimal.valueOf(0), "url", Gender.FEMALE);
            teacher.getRoles().add(teacherRole);

            SuperAdmin superAdmin = new SuperAdmin("Samuel", passwordEncoder.encode("12345"), "sam@gmail.com", "999-33-8989", BigDecimal.valueOf(0));
            superAdmin.getRoles().add(superAdminRole);

//            roleDao.save(studentRole);
//            roleDao.save(teacherRole);
//            roleDao.save(superAdminRole);
//            roleDao.save(parentOrGuardianRole);

//           StudentParent studentParent = studentParentDao.getReferenceById(1);
//       ParentOrGuardian parent = parentOrGuardianDao.getReferenceById(8);
//       student.addParent(studentParent);
//       studentDao.save(student);
//       studentParentDao.save(studentParent);

            // 1. Fetch existing student and parent (or create new ones)
            ParentOrGuardian parent = parentOrGuardianDao.findById(7).orElseThrow(() -> new ParentOrGuardianNotFoundException(8));
            Student student = studentDao.findById(1).orElseThrow(() -> new StudentNotFoundException("Student not found with id: 1"));
            // 2. Create a new StudentParent association
            StudentParent studentParent = new StudentParent(parent, student);
            // 3. Set up the bidirectional relationship
            student.addParent(studentParent);
            parent.addStudent(studentParent);

            // 4. Persist the association and entities
          //  studentParentDao.save(studentParent);

            parentOrGuardianDao.save(parent);


//            parentOrGuardianDao.save(parentOrGuardian);
//            teacherDao.save(teacher);
//            superAdminDao.save(superAdmin);

        };

    }


    public static void main(String[] args) {
        SpringApplication.run(AthenaBackendApplication.class, args);
    }

}
