package com.soumya.main.controllers;

import com.soumya.main.config.CustomUserDetails;
import com.soumya.main.entity.User;
import com.soumya.main.repository.UserRepository;
import com.soumya.main.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;

    @GetMapping
    public String openDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        User user = userRepository.findById(userDetails.getId()).orElseThrow();

        model.addAttribute("totalIncome", dashboardService.getTotalIncome(user));
        model.addAttribute("totalExpense", dashboardService.getTotalExpense(user));
        model.addAttribute("balance", dashboardService.getBalance(user));
        model.addAttribute("transactions", dashboardService.getRecentTransactions(user));
        model.addAttribute("categorySummary", dashboardService.getCategorySummary(user));

        return "dashboard";
    }
}

