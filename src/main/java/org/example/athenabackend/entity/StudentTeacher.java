package org.example.athenabackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "student_teacher")
public class StudentTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public StudentTeacher(){}
    public StudentTeacher(Teacher teacher, Student student){
        this.teacher = teacher;
        this.student = student;
    }
}
