package com.soumya.main.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@AllArgsConstructor
public class BudgetProgressDTO {

    private String category;
    private BigDecimal budgetedAmount;
    private BigDecimal spentAmount;
    private BigDecimal remaining;
    private double percentageUsed;
    private String cssClass; // success, warning, danger

    public static BudgetProgressDTO of(
            String category,
            BigDecimal budget,
            BigDecimal spent
    ) {
        budget = budget == null ? BigDecimal.ZERO : budget;
        spent = spent == null ? BigDecimal.ZERO : spent;

        BigDecimal remaining = budget.subtract(spent);

        double percent = budget.signum() == 0
                ? 0
                : spent.divide(budget, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();

        String css = percent >= 100 ? "bg-danger"
                : percent >= 75 ? "bg-warning"
                : "bg-success";

        return new BudgetProgressDTO(
                category,
                budget,
                spent,
                remaining,
                Math.min(percent, 100),
                css
        );
    }
}
