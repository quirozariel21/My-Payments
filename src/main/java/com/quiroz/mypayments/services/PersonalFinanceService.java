package com.quiroz.mypayments.services;

import com.quiroz.mypayments.dto.requests.AddPersonalFinanceRequestDto;
import com.quiroz.mypayments.dto.requests.UpdatePersonalFinanceRequestDto;
import com.quiroz.mypayments.dto.responses.PersonalFinanceResponseDto;
import com.quiroz.mypayments.dto.responses.TotalResponseDto;
import com.quiroz.mypayments.enums.Month;

public interface PersonalFinanceService {

    PersonalFinanceResponseDto savePersonalFinance(AddPersonalFinanceRequestDto requestDto);

    PersonalFinanceResponseDto updatePersonalFinance(UpdatePersonalFinanceRequestDto requestDto);

    void deletePersonalFinance(Long id);

    PersonalFinanceResponseDto getById(Long id);

    TotalResponseDto getTotals(Long personalFinanceId);

}
