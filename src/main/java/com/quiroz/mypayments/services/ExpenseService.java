package com.quiroz.mypayments.services;

import com.quiroz.mypayments.dto.requests.AddExpenseRequestDto;
import com.quiroz.mypayments.dto.responses.ExpenseResponseDto;
import com.quiroz.mypayments.entities.PersonalFinance;

import java.math.BigDecimal;

public interface ExpenseService {
    ExpenseResponseDto save(AddExpenseRequestDto addExpenseRequestDto);

    BigDecimal sumTotalSpentByPersonalFinanceId(Long personalFinanceId);
}
