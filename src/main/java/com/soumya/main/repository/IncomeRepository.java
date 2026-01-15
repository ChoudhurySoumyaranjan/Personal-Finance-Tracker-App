package com.soumya.main.repository;

import com.soumya.main.entity.Income;
import com.soumya.main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUser(User user);
    List<Income> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    List<Income> findByUserAndSource(User user, String source);
    List<Income> findByUserAndSourceAndDateBetween(User user, String source, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.user = :user")
    BigDecimal getTotalIncome(User user);


    List<Income> findTop5ByUserOrderByDateDesc(User user);
    @Query("""
   SELECT i FROM Income i
   WHERE i.user = :user
   AND (:start IS NULL OR i.date >= :start)
   AND (:end IS NULL OR i.date <= :end)
   AND (:keyword IS NULL OR LOWER(i.source) LIKE LOWER(CONCAT('%', :keyword, '%')))
   ORDER BY i.date DESC
""")
    List<Income> searchIncomes(
            @Param("user") User user,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("keyword") String keyword
    );

}
