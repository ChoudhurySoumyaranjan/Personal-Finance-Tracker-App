package com.soumya.main.entity;

import com.soumya.main.dtos.YearMonthStringConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;

import jakarta.persistence.*;
import java.time.YearMonth;
import java.math.BigDecimal;
import jakarta.validation.constraints.*;


@Entity
@Data
@Table(name = "budgets",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "category", "year_month"}
        ))
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String category;

    @Column(name = "`year_month`") // ⬅️ ESCAPED
    private String yearMonth;

    @Column(precision = 10, scale = 2)
    private BigDecimal budgetedAmount;
}
