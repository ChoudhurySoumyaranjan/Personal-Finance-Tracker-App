package com.soumya.main.repository;

import com.soumya.main.entity.Budget;
import com.soumya.main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserAndYearMonth(User user, String yearMonth);
    Optional<Budget> findByUserIdAndCategoryAndYearMonth(
            Long userId,
            String category,
            String yearMonth
    );
    List<Budget> findByUserIdAndYearMonth(Long userId, String yearMonth);
}

