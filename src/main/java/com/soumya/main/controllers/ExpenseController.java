package com.soumya.main.controllers;

import com.soumya.main.entity.Expense;
import com.soumya.main.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    private static final List<String> CATEGORIES = List.of("Groceries", "Utilities", "Entertainment", "Transportation", "Health", "Others");
    @GetMapping
    public String listExpenses(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String category,
            Model model) {
        List<Expense> expenses = expenseService.getFilteredExpenses(startDate, endDate, category);
        model.addAttribute("expenses", expenses);
        model.addAttribute("categories", CATEGORIES);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("selectedCategory", category);
        return "expense-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("expense", new Expense());
        model.addAttribute("categories", CATEGORIES);
        return "expense-form";
    }

    @PostMapping("/add")
    public String addExpense(@Valid @ModelAttribute Expense expense, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", CATEGORIES);
            return "expense-form";
        }
        expenseService.addExpense(expense);
        return "redirect:/expenses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Expense expense = expenseService.getExpenseById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        model.addAttribute("expense", expense);
        model.addAttribute("categories", CATEGORIES);
        return "expense-form";
    }

    @PostMapping("/edit/{id}")
    public String updateExpense(@PathVariable Long id, @Valid @ModelAttribute Expense expense, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", CATEGORIES);
            return "expense-form";
        }
        expenseService.updateExpense(id, expense);
        return "redirect:/expenses";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return "redirect:/expenses";
    }
}
