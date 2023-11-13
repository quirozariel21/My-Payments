package com.quiroz.mypayments.repositories;

import com.quiroz.mypayments.entities.PersonalFinance;
import com.quiroz.mypayments.enums.Month;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalFinanceRepository extends JpaRepository<PersonalFinance, Long> {

    Optional<PersonalFinance> findByYearAndMonth(int year, Month month);
}
