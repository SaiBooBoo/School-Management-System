package org.example.athenabackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.athenabackend.exception.ParentOrGuardianNotFoundException;
import org.example.athenabackend.model.AttendanceStatus;
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
@Table(name = "students")
@DiscriminatorValue("STUDENT")
@NoArgsConstructor
@ToString
public class Student extends User{

    @Column(name = "display_name", nullable = false)
    private String displayName;
    private LocalDate dob;
    private String address;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender = Gender.OTHER;
    @Column(name = "profile_image_path")
    private String profileImagePath;
    private BigDecimal grade;


    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<StudentTeacher> teachers = new ArrayList<StudentTeacher>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<StudentParent> parents = new ArrayList<StudentParent>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Attendance> attendanceRecords = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exam> exams = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fee> fees = new ArrayList<>();

    @ManyToMany(mappedBy = "students")
    private Set<Classroom> classrooms = new HashSet<>();

    public Student(String username, String password,String displayName, Gender gender, LocalDate dob, String address, BigDecimal grade) {
        super(username, password);
        this.dob = dob;
        this.address = address;
        this.displayName = displayName;
        this.gender = gender;
        this.grade = grade;
    }

    public void addFee(Fee fee) {
        fees.add(fee);
        fee.setStudent(this);
    }

    public void addExam(Exam exam){
        exams.add(exam);
        exam.setStudent(this);
    }

    public void addAttendance(LocalDate date, AttendanceStatus status){
        Attendance a = new Attendance(date, status, this);
        attendanceRecords.add(a);
    }

    public void addTeacher(LocalDate date, AttendanceStatus status){
        Attendance a = new Attendance(date, status, this);
        attendanceRecords.add(a);
    }

    public void addParent(StudentParent studentParent){
       this.parents.add(studentParent);
       studentParent.setStudent(this);
       // beware of this line because it is an AI generated slop
       studentParent.setParentOrGuardian(studentParent.getParentOrGuardian());
    }

    public void removeParent(ParentOrGuardian parentOrGuardian){
            StudentParent toRemove = parents
                    .stream()
                    .filter(sp -> sp.getParentOrGuardian().equals(parentOrGuardian))
                    .findFirst()
                    .orElseThrow(() -> new ParentOrGuardianNotFoundException("Parent not found in student."));
        if(toRemove != null){
            parents.remove(toRemove);
        }
    }

    public void addStudentTeacher(StudentTeacher studentTeacher){
        this.teachers.add(studentTeacher);
        studentTeacher.setStudent(this);
    }
}
