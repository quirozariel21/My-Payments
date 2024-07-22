package com.quiroz.mypayments.repositories;

import com.quiroz.mypayments.entities.Income;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query("SELECT SUM(ti.amount) FROM Income ti WHERE ti.personalFinance.id = ?1")
    BigDecimal sumTotalReceivedByPersonalFinanceId(Long personalFinanceId);

    @Query("SELECT in FROM Income in WHERE in.id = ?1 AND in.personalFinance.id =?2")
    Optional<Income> findByIdAndPersonalFinanceId(Long incomeId, Long personalFinanceId);
}
