package org.example.athenabackend.dtoSummaries;

import java.math.BigDecimal;

public record FeeSummaryDto(
        int month,
        int year,
        BigDecimal totalUnpaid) {
}
