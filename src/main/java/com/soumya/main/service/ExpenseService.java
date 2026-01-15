package com.soumya.main.service;

import com.soumya.main.entity.Expense;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseService {
    Expense addExpense(Expense expense) ;
    List<Expense> getAllExpenses();
    public List<Expense> getFilteredExpenses(LocalDate startDate, LocalDate endDate, String category);
    Optional<Expense> getExpenseById(Long id) ;
    Expense updateExpense(Long id, Expense updated) ;
    void deleteExpense(Long id) ;
}
