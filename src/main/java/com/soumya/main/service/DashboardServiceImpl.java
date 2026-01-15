package com.soumya.main.service;

import com.soumya.main.dtos.CategorySummaryDTO;
import com.soumya.main.dtos.TransactionDTO;
import com.soumya.main.entity.Expense;
import com.soumya.main.entity.Income;
import com.soumya.main.entity.User;
import com.soumya.main.repository.ExpenseRepository;
import com.soumya.main.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    @Override
    public BigDecimal getTotalIncome(User user) {
        return incomeRepository.getTotalIncome(user);
    }

    @Override
    public BigDecimal getTotalExpense(User user) {
        return expenseRepository.getTotalExpense(user);
    }

    @Override
    public BigDecimal getBalance(User user) {
        return getTotalIncome(user).subtract(getTotalExpense(user));
    }



    @Override
    public List<TransactionDTO> getRecentTransactions(User user) {
        List<TransactionDTO> list = new ArrayList<>();

        List<Income> incomes = incomeRepository.findTop5ByUserOrderByDateDesc(user);
        List<Expense> expenses = expenseRepository.findTop5ByUserOrderByDateDesc(user);

        incomes.forEach(i ->
                list.add(new TransactionDTO(
                        i.getDate(),
                        "INCOME",
                        i.getSource(),
                        BigDecimal.valueOf(i.getAmount()),
                        i.getDescription()
                ))
        );

        expenses.forEach(e ->
                list.add(new TransactionDTO(
                        e.getDate(),
                        "EXPENSE",
                        e.getCategory(),
                        BigDecimal.valueOf(e.getAmount()),
                        e.getDescription()
                ))
        );

        list.sort(Comparator.comparing(TransactionDTO::getDate).reversed());
        return list.stream().limit(5).toList();
    }


    @Override
    public List<CategorySummaryDTO> getCategorySummary(User user) {
        List<Object[]> raw = expenseRepository.getCategorySummary(user);
        List<CategorySummaryDTO> summary = new ArrayList<>();

        for (Object[] row : raw) {
            summary.add(new CategorySummaryDTO(
                    (String) row[0],
                    (BigDecimal) row[1]
            ));
        }
        return summary;
    }
}

