package com.soumya.main.service;

import com.soumya.main.dtos.BudgetFormDTO;
import com.soumya.main.dtos.BudgetProgressDTO;
import com.soumya.main.entity.Budget;
import com.soumya.main.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BudgetService {

    void setBudget(Long userId, String category, String month, BigDecimal amount);

    List<BudgetProgressDTO> getBudgetProgress(Long userId, String monthYear);
}
