package com.soumya.main.service;

import com.soumya.main.dtos.TransactionDTO;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    List<TransactionDTO> getTransactions(
            LocalDate start,
            LocalDate end,
            String keyword
    );
}
