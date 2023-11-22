package com.quiroz.mypayments.repositories;

import com.quiroz.mypayments.entities.Expense;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT SUM(te.amount) FROM Expense te WHERE te.personalFinance.id = ?1")
    BigDecimal sumTotalSpentByPersonalFinanceId(Long personalFinanceId);

    @Query("SELECT ex FROM Expense ex WHERE ex.id = ?1 AND ex.personalFinance.id = ?2")
    Optional<Expense> findByIdAndPersonalFinanceId(Long id, Long personalFinanceId);
}
