package com.quiroz.mypayments.services;

import com.quiroz.mypayments.dto.requests.AddExpenseRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateExpenseRequestDto;
import com.quiroz.mypayments.dto.responses.ExpenseResponseDto;
import java.math.BigDecimal;

public interface ExpenseService {
    ExpenseResponseDto save(AddExpenseRequestDto addExpenseRequestDto);

    ExpenseResponseDto update(UpdateExpenseRequestDto requestDto);

    void delete(Long expenseId);

    BigDecimal sumTotalSpentByPersonalFinanceId(Long personalFinanceId);
}
