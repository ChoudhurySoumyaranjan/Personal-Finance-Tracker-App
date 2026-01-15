package com.soumya.main.service;

import com.soumya.main.dtos.TransactionDTO;
import com.soumya.main.entity.User;
import com.soumya.main.repository.ExpenseRepository;
import com.soumya.main.repository.IncomeRepository;
import com.soumya.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private IncomeRepository incomeRepository;
    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<TransactionDTO> getTransactions(
            LocalDate start,
            LocalDate end,
            String keyword
    ) {
        User user = getCurrentUser();

        List<TransactionDTO> expenses =
                expenseRepository.searchExpenses(user, start, end, keyword)
                        .stream()
                        .map(e -> new TransactionDTO(
                                e.getDate(),
                                "EXPENSE",
                                e.getCategory(),
                                BigDecimal.valueOf(e.getAmount()),
                                e.getDescription()
                        ))
                        .toList();

        List<TransactionDTO> incomes =
                incomeRepository.searchIncomes(user, start, end, keyword)
                        .stream()
                        .map(i -> new TransactionDTO(
                                i.getDate(),
                                "INCOME",
                                i.getSource(),
                                BigDecimal.valueOf(i.getAmount()),
                                i.getDescription()
                        ))
                        .toList();

        return Stream.concat(expenses.stream(), incomes.stream())
                .sorted(Comparator.comparing(TransactionDTO::getDate).reversed())
                .toList();
    }
}

