package org.example.athenabackend.dto;

import lombok.*;
import org.example.athenabackend.model.FeeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeeDto {
    private Integer id;
    private int studentId;
    private BigDecimal amount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private FeeStatus status;
    private String remarks;
}
