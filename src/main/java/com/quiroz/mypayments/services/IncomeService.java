package com.quiroz.mypayments.services;

import com.quiroz.mypayments.dto.requests.AddIncomeRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateIncomeRequestDto;
import com.quiroz.mypayments.dto.responses.IncomeResponseDto;

import java.math.BigDecimal;

public interface IncomeService {

    IncomeResponseDto addIncome(AddIncomeRequestDto requestDto);
    IncomeResponseDto updateIncome(Long personalFinanceId, UpdateIncomeRequestDto requestDto);
    void deleteIncome(Long personalFinanceId, Long incomeId);
    BigDecimal sumTotalReceivedByPersonalFinanceId(Long personalFinanceId);
}
