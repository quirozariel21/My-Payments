package com.quiroz.mypayments.services;

import com.quiroz.mypayments.dto.requests.AddIncomeRequestDto;
import com.quiroz.mypayments.dto.responses.IncomeResponseDto;

import java.math.BigDecimal;

public interface IncomeService {

    IncomeResponseDto addIncome(AddIncomeRequestDto requestDto);
    BigDecimal sumTotalReceivedByPersonalFinanceId(Long personalFinanceId);
}
