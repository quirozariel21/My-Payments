package com.quiroz.mypayments.dto.responses;

import com.quiroz.mypayments.enums.Currency;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
