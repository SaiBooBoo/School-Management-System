package org.example.athenabackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.athenabackend.exception.StudentNotFoundException;
import org.example.athenabackend.model.Gender;
import org.example.athenabackend.model.ParentType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "parent_or_guardian")
public class ParentOrGuardian extends User{
    @Column(name = "display_name", nullable = false)
    private String displayName;
    @Column(name = "nrc_no", nullable = false)
    private String nrcNumber;
    private LocalDate dob;
    private String job;
    private Gender gender = Gender.OTHER;
    private String phoneNumber;
    private String address;
    @Enumerated(EnumType.STRING)
    @Column(name = "parent_type", nullable = false)
    private ParentType parentType = ParentType.OTHER;
    @Column(name="profile_image_path")
    private String profileImagePath;

    @JsonManagedReference // Prevents infinite loop when serializing Parent -> StudentParent
    @OneToMany(mappedBy = "parentOrGuardian", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<StudentParent> students = new ArrayList<StudentParent>();

    public ParentOrGuardian(String username, String password, String displayName, Gender gender, String nrcNumber, LocalDate dob, String job, String phoneNumber, String address, ParentType parentType, String profileImagePath) {
        super(username, password);
        this.displayName = displayName;
        this.nrcNumber = nrcNumber;
        this.dob = dob;
        this.job = job;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.parentType = parentType;
        this.profileImagePath = profileImagePath;
        this.gender = gender;
    }

    public void addStudent(StudentParent studentParent) {
        this.students.add(studentParent);
        studentParent.setParentOrGuardian(this);
    }

    public void removeStudent(Student student){
        StudentParent toRemove = students
                .stream()
                .filter(sp -> sp.getStudent().equals(student))
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException("Student not found in parents."));
        if (toRemove != null){
            students.remove(toRemove);
            student.getParents().remove(toRemove);
        }
    }

}
