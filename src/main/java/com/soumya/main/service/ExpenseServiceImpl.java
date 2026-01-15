package com.soumya.main.service;

import com.soumya.main.entity.Expense;
import com.soumya.main.entity.User;
import com.soumya.main.repository.ExpenseRepository;
import com.soumya.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();  // Email as username
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Add new expense
    public Expense addExpense(Expense expense) {
        User user = getCurrentUser();
        expense.setUser(user);
        return expenseRepository.save(expense);
    }

    // Get all expenses for current user (unfiltered)
    public List<Expense> getAllExpenses() {
        User user = getCurrentUser();
        return expenseRepository.findByUser(user);
    }

    // New: Get filtered expenses (by date range and/or category)
    public List<Expense> getFilteredExpenses(LocalDate startDate, LocalDate endDate, String category) {
        User user = getCurrentUser();
        if (startDate != null && endDate != null && category != null && !category.isEmpty()) {
            return expenseRepository.findByUserAndCategoryAndDateBetween(user, category, startDate, endDate);
        } else if (startDate != null && endDate != null) {
            return expenseRepository.findByUserAndDateBetween(user, startDate, endDate);
        } else if (category != null && !category.isEmpty()) {
            return expenseRepository.findByUserAndCategory(user, category);
        } else {
            return getAllExpenses();  // Default to all if no filters
        }
    }

    // Get expense by ID (only if owned by current user)
    public Optional<Expense> getExpenseById(Long id) {
        User user = getCurrentUser();
        return expenseRepository.findById(id).filter(e -> e.getUser().equals(user));
    }

    // Update expense
    public Expense updateExpense(Long id, Expense updated) {
        Expense existing = getExpenseById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        existing.setAmount(updated.getAmount());
        existing.setCategory(updated.getCategory());
        existing.setDate(updated.getDate());
        existing.setDescription(updated.getDescription());
        return expenseRepository.save(existing);
    }

    // Delete expense
    public void deleteExpense(Long id) {
        Expense expense = getExpenseById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        expenseRepository.delete(expense);
    }
}
