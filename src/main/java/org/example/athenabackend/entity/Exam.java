package org.example.athenabackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.athenabackend.model.Subject;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "exam")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String examName;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Subject subject;
    @Column(name = "exam_date")
    private LocalDate examDate;
    @Column(nullable = false)
    private Double score;
    @Column(nullable = false)
    private Double maxScore;
    private String grade;
    private String remarks;

    public Exam(Student student,
                String examName,
                Subject subject,
                LocalDate examDate,
                Double score,
                Double maxScore,
                String grade,
                String remarks) {
        this.student  = student;
        this.examName = examName;
        this.subject  = subject;
        this.examDate     = examDate;
        this.score    = score;
        this.maxScore = maxScore;
        this.grade    = grade;
        this.remarks  = remarks;
    }
}
