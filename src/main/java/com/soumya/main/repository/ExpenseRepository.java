package com.soumya.main.repository;

import com.soumya.main.entity.Expense;
import com.soumya.main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
    List<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    List<Expense> findByUserAndCategory(User user, String category);
    List<Expense> findByUserAndCategoryAndDateBetween(User user, String category, LocalDate startDate, LocalDate endDate);
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user = :user")
    BigDecimal getTotalExpense(User user);


    List<Expense> findTop5ByUserOrderByDateDesc(User user);

    @Query("""
        SELECT e.category, SUM(e.amount)
        FROM Expense e
        WHERE e.user = :user
        GROUP BY e.category
    """)
    List<Object[]> getCategorySummary(User user);

    @Query("""
        SELECT e.category, SUM(e.amount)
        FROM Expense e
        WHERE e.user.id = :userId
          AND e.date BETWEEN :start AND :end
        GROUP BY e.category
    """)
    List<Object[]> findTotalSpentPerCategoryByUserAndMonth(
            @Param("userId") Long userId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
   SELECT e FROM Expense e
   WHERE e.user = :user
   AND (:start IS NULL OR e.date >= :start)
   AND (:end IS NULL OR e.date <= :end)
   AND (:keyword IS NULL OR LOWER(e.category) LIKE LOWER(CONCAT('%', :keyword, '%')))
   ORDER BY e.date DESC
""")
    List<Expense> searchExpenses(
            @Param("user") User user,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("keyword") String keyword
    );
}
