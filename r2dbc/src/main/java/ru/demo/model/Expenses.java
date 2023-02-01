package ru.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record Expenses(
        Long id,
        BigDecimal expensesSum,
        String expensesComment,
        LocalDate expensesDate,

        LocalDateTime processedAt,
        LocalDateTime createdAt,
        LocalDate createdDayAt
) {
}
