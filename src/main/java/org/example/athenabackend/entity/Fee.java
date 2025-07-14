package org.example.athenabackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.athenabackend.model.FeeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "fee")
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY, optional= false)
    @JoinColumn(name="student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate dueDate;
    private LocalDate paymentDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable =false)
    private FeeStatus status;

    private String remarks;
    public Fee(Student student,
               BigDecimal amount,
               LocalDate dueDate,
               FeeStatus status,
               LocalDate paymentDate,
               String remarks) {
        this.student     = student;
        this.amount      = amount;
        this.dueDate     = dueDate;
        this.status      = status;
        this.paymentDate = paymentDate;
        this.remarks     = remarks;
    }
}
