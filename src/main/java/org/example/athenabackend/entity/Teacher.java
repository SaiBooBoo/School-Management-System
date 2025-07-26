package org.example.athenabackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.athenabackend.dtoSummaries.StudentSummaryDto;
import org.example.athenabackend.exception.StudentNotFoundException;
import org.example.athenabackend.model.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("TEACHER")
@NoArgsConstructor
@Table(name = "teacher")
public class Teacher extends User{

    private String displayName;
    @Column(name = "nrc_no")
    private String nrcNumber;
    private String qualification;
    private LocalDate dob;
    private String phone;
    private String address;
    private BigDecimal earning;
    @Column(name = "profile_image_path")
    private String profileImagePath;
    private Gender gender = Gender.OTHER;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER )
    private List<StudentTeacher> students = new ArrayList<StudentTeacher>();

    @ManyToMany(mappedBy = "teachers")
    private Set<Classroom> classrooms = new HashSet<Classroom>();

    @ManyToMany
    @JoinTable(name= "teacher_subject", joinColumns = @JoinColumn(name = "teacher_id"),
    inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects = new HashSet<Subject>();

    public Teacher(String username, String password, String displayName,String nrcNumber,
                   String qualification, LocalDate dob, String phone,
                   String address, BigDecimal earning, String profileImagePath, Gender gender) {
        super(username, password);
        this.displayName = displayName;
        this.nrcNumber = nrcNumber;
        this.qualification = qualification;
        this.dob = dob;
        this.phone = phone;
        this.address = address;
        this.earning = earning;
        this.profileImagePath = profileImagePath;
        this.gender = gender;
    }

    public void addStudentTeacher(StudentTeacher studentTeacher){
        this.students.add(studentTeacher);
        studentTeacher.setTeacher(this);
    }

    public void addStudent(StudentTeacher studentTeacher) {
        this.students.add(studentTeacher);
        studentTeacher.setTeacher(this);
        studentTeacher.setTeacher(studentTeacher.getTeacher());
    }

    public void removeStudent(StudentTeacher studentTeacher) {
        StudentTeacher toRemove = students
                .stream()
                .filter(st -> st.getStudent().equals(studentTeacher.getStudent()))
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException("Student not found in teacher."));
        if (toRemove != null) {
            students.remove(toRemove);
        }
    }
}
