package org.example.athenabackend.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MassFeeRequest (
        BigDecimal amount,
        LocalDate dueDate,
        String remarks){
}
