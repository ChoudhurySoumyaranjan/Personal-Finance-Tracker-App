package com.soumya.main.service;

import com.soumya.main.dtos.CategorySummaryDTO;
import com.soumya.main.dtos.TransactionDTO;
import com.soumya.main.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardService {
    BigDecimal getTotalIncome(User user);

    BigDecimal getTotalExpense(User user);

    BigDecimal getBalance(User user);

    List<TransactionDTO> getRecentTransactions(User user);

    List<CategorySummaryDTO> getCategorySummary(User user);
}
