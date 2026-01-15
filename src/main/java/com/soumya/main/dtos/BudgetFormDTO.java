package com.soumya.main.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetFormDTO {
    private Long id;
    private String category;
    private BigDecimal amount;
    private String yearMonth; // "2026-01" format from frontend

}
