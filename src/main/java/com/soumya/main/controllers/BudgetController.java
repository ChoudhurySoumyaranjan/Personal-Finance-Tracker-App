package com.soumya.main.controllers;

import com.soumya.main.config.CustomUserDetails;
import com.soumya.main.dtos.BudgetFormDTO;
import com.soumya.main.dtos.BudgetProgressDTO;
import com.soumya.main.entity.User;
import com.soumya.main.repository.UserRepository;
import com.soumya.main.service.BudgetService;
import com.soumya.main.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService service;

    private static final List<String> CATEGORIES =
            List.of("Groceries", "Utilities", "Entertainment", "Transportation", "Health", "Others");

    @GetMapping
    public String budgets(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String monthYear,
            Model model
    ) {

        if (monthYear == null) {
            monthYear = YearMonth.now().toString();
        }

        model.addAttribute("progress",
                service.getBudgetProgress(user.getId(), monthYear));
        model.addAttribute("categories", CATEGORIES);
        model.addAttribute("monthYear", monthYear);

        return "budgets";
    }

    @PostMapping("/set")
    public String setBudget(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam String category,
            @RequestParam BigDecimal amount,
            @RequestParam String monthYear
    ) {
        service.setBudget(user.getId(), category, monthYear, amount);
        return "redirect:/budgets?monthYear=" + monthYear;
    }
}
