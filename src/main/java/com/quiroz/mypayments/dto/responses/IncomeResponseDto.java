package com.quiroz.mypayments.dto.responses;

import com.quiroz.mypayments.enums.Currency;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;


@Getter
@Setter
@Builder
public class IncomeResponseDto {

    private long id;
    private String name;
    private BigDecimal amount;
    private Currency currency;
    private PersonalFinanceResponseDto personalFinance;
}
