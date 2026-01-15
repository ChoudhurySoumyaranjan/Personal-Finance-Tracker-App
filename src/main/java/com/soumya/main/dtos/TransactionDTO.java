package com.soumya.main.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TransactionDTO {

    private LocalDate date;
    private String type;        // INCOME or EXPENSE
    private String category;    // source/category
    private BigDecimal amount;
    private String description;
}

