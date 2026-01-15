package com.soumya.main.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;


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
