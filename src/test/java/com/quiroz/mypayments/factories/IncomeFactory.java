package com.quiroz.mypayments.factories;

import com.quiroz.mypayments.dto.requests.AddIncomeRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateIncomeRequestDto;
import com.quiroz.mypayments.entities.Income;
import com.quiroz.mypayments.enums.Currency;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IncomeFactory {

    public static final Long ID = 1L;
    public static final String NAME = "INCOME NAME TEST";
    public static final BigDecimal AMOUNT = new BigDecimal("1.5");
    public static final Currency CURRENCY = Currency.BOB;
    public static final Long PERSONAL_FINANCE_ID = 10L;

    public static AddIncomeRequestDto createAddIncomeRequestDto() {
        return AddIncomeRequestDto.builder()
            .name(NAME)
            .amount(AMOUNT)
            .currency(CURRENCY)
            .personalFinanceId(PersonalFinanceFactory.ID)
            .build();
    }

    public static UpdateIncomeRequestDto createUpdateIncomeRequestDto() {
        return UpdateIncomeRequestDto.builder()
            .id(ID)
            .name(NAME)
            .amount(AMOUNT)
            .currency(CURRENCY)
            .personalFinanceId(PersonalFinanceFactory.ID)
            .build();
    }

    public static Income createIncome() {
        return Income.builder()
            .id(ID)
            .name(NAME)
            .amount(AMOUNT)
            .currency(CURRENCY)
            .personalFinance(PersonalFinanceFactory.createPersonalFinance())
            .build();
    }
}
