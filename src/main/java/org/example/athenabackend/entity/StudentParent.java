package org.example.athenabackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "student_parent")
public class StudentParent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private ParentOrGuardian parentOrGuardian;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public StudentParent(){}
    public StudentParent(ParentOrGuardian parentOrGuardian, Student student) {
        this.parentOrGuardian = parentOrGuardian;
        this.student = student;
    }
}
