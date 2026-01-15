package com.soumya.main.service;

import com.soumya.main.entity.Income;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IncomeService {
    Income addIncome(Income income);
    List<Income> getAllIncomes();
    List<Income> getFilteredIncomes(LocalDate startDate, LocalDate endDate, String source);
    Optional<Income> getIncomeById(Long id);
    Income updateIncome(Long id, Income updated);
    void deleteIncome(Long id);
}
