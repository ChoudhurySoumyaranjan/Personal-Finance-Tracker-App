package com.soumya.main.controllers;

import com.soumya.main.entity.Income;
import com.soumya.main.service.IncomeService;
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
@RequestMapping("/incomes")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    private static final List<String> SOURCES = List.of("Salary","Bonus","Freelance","Investment","Others");

    private static final List<String> FREQUENCIES = List.of("One-time", "Weekly", "Monthly", "Quarterly", "Annually");

    @GetMapping
    public String listIncomes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String source,
            Model model) {
        List<Income> incomes = incomeService.getFilteredIncomes(startDate, endDate, source);
        model.addAttribute("incomes", incomes);
        model.addAttribute("sources", SOURCES);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("selectedSource", source);
        return "income-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("income", new Income());
        model.addAttribute("sources", SOURCES);
        model.addAttribute("frequencies", FREQUENCIES);
        return "income-form";
    }
    @PostMapping("/add")
    public String addIncome(@Valid @ModelAttribute Income income, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("sources", SOURCES);
            model.addAttribute("frequencies", FREQUENCIES);
            return "income-form";
        }
        incomeService.addIncome(income);
        return "redirect:/incomes";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Income income = incomeService.getIncomeById(id).orElseThrow(() -> new RuntimeException("Income not found"));
        model.addAttribute("income", income);
        model.addAttribute("sources", SOURCES);
        model.addAttribute("frequencies", FREQUENCIES);
        return "income-form";
    }
    @PostMapping("/edit/{id}")
    public String updateIncome(@PathVariable Long id, @Valid @ModelAttribute Income income, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("sources", SOURCES);
            model.addAttribute("frequencies", FREQUENCIES);
            return "income-form";
        }
        incomeService.updateIncome(id, income);
        return "redirect:/incomes";
    }
    @GetMapping("/delete/{id}")
    public String deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return "redirect:/incomes";
    }
}
