package com.quiroz.mypayments.services;

import com.quiroz.mypayments.dto.requests.AddPersonalFinanceRequestDto;
import com.quiroz.mypayments.dto.responses.PersonalFinanceResponseDto;
import com.quiroz.mypayments.dto.responses.TotalResponseDto;
import com.quiroz.mypayments.enums.Month;

public interface PersonalFinanceService {

    PersonalFinanceResponseDto savePersonalFinance(AddPersonalFinanceRequestDto requestDto);

    TotalResponseDto getTotals(Long personalFinanceId);

}
