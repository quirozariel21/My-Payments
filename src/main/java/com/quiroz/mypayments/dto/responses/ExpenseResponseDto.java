package com.quiroz.mypayments.dto.responses;

import com.quiroz.mypayments.enums.Currency;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
public class ExpenseResponseDto {
    //TODO add subcategoryDto
    private long id;
    private CategoryResponseDto category;
    private String note;
    private BigDecimal amount;
    private Currency currency;
    private int number;

}
