package com.quiroz.mypayments.dto.requests;

import com.quiroz.mypayments.enums.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
public class AddIncomeRequestDto {
    @NotNull
    private String name;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
    @NotNull
    private Currency currency;
    @NotNull
    private Long personalFinanceId;
}
