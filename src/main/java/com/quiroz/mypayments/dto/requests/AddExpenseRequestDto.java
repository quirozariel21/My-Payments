package com.quiroz.mypayments.dto.requests;

import com.quiroz.mypayments.enums.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddExpenseRequestDto {

    @NotNull private Long categoryId;
    @NotNull private Long subcategoryId;
    private String note;

    @DecimalMin("0.01")
    private BigDecimal amount;

    private Currency currency;
    @NotNull private LocalDate expensedDate;
    private Long personalFinanceId;
}
