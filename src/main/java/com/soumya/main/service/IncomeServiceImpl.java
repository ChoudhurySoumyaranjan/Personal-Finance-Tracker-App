package com.soumya.main.service;

import com.soumya.main.entity.Income;
import com.soumya.main.entity.User;
import com.soumya.main.repository.IncomeRepository;
import com.soumya.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Income addIncome(Income income) {
        User user = getCurrentUser();
        income.setUser(user);
        return incomeRepository.save(income);
    }

    @Override
    public List<Income> getAllIncomes() {
        User user = getCurrentUser();
        return incomeRepository.findByUser(user);
    }

    @Override
    public List<Income> getFilteredIncomes(LocalDate startDate, LocalDate endDate, String source) {
        User user = getCurrentUser();
        if (startDate != null && endDate != null && source != null && !source.isEmpty()) {
            return incomeRepository.findByUserAndSourceAndDateBetween(user, source, startDate, endDate);
        } else if (startDate != null && endDate != null) {
            return incomeRepository.findByUserAndDateBetween(user, startDate, endDate);
        } else if (source != null && !source.isEmpty()) {
            return incomeRepository.findByUserAndSource(user, source);
        } else {
            return getAllIncomes();
        }
    }

    @Override
    public Optional<Income> getIncomeById(Long id) {
        User user = getCurrentUser();
        return incomeRepository.findById(id).filter(i -> i.getUser().equals(user));
    }

    @Override
    public Income updateIncome(Long id, Income updated) {
        Income existing = getIncomeById(id).orElseThrow(() -> new RuntimeException("Income not found"));
        existing.setAmount(updated.getAmount());
        existing.setSource(updated.getSource());
        existing.setDate(updated.getDate());
        existing.setFrequency(updated.getFrequency());
        existing.setDescription(updated.getDescription());
        return incomeRepository.save(existing);
    }

    @Override
    public void deleteIncome(Long id) {
        Income income = getIncomeById(id).orElseThrow(() -> new RuntimeException("Income not found"));
        incomeRepository.delete(income);
    }
}
