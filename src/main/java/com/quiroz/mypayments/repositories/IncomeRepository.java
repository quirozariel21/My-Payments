package com.quiroz.mypayments.repositories;

import com.quiroz.mypayments.entities.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query("SELECT SUM(ti.amount) FROM Income ti WHERE ti.personalFinance.id = ?1")
    BigDecimal sumTotalReceivedByPersonalFinanceId(Long personalFinanceId);
}
