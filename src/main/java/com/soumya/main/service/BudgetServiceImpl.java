package com.soumya.main.service;

import com.soumya.main.dtos.BudgetProgressDTO;
import com.soumya.main.entity.Budget;
import com.soumya.main.entity.User;
import com.soumya.main.repository.BudgetRepository;
import com.soumya.main.repository.ExpenseRepository;
import com.soumya.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService{

    @Autowired
    public BudgetRepository budgetRepo;

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void setBudget(Long userId, String category, String month, BigDecimal amount) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = budgetRepo
                .findByUserIdAndCategoryAndYearMonth(userId, category, month)
                .orElse(new Budget());

        budget.setUser(user);
        budget.setCategory(category);
        budget.setYearMonth(month);
        budget.setBudgetedAmount(amount);

        budgetRepo.save(budget);
    }


    @Override
    public List<BudgetProgressDTO> getBudgetProgress(Long userId, String monthYear) {

        YearMonth ym = YearMonth.parse(monthYear);

        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();


        Map<String, BigDecimal> spentMap =
                expenseRepository
                        .findTotalSpentPerCategoryByUserAndMonth(
                                userId,
                                start,   // ✅ LocalDate
                                end      // ✅ LocalDate
                        )
                        .stream()
                        .collect(Collectors.toMap(
                                r -> (String) r[0],
                                r -> BigDecimal.valueOf(((Number) r[1]).doubleValue())
                        ));



        List<Budget> budgets =
                budgetRepo.findByUserIdAndYearMonth(userId, monthYear);

        return budgets.stream()
                .map(b -> BudgetProgressDTO.of(
                        b.getCategory(),
                        b.getBudgetedAmount(),
                        spentMap.getOrDefault(b.getCategory(), BigDecimal.ZERO)
                ))
                .toList();
    }
}
