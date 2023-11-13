package com.quiroz.mypayments.dto.responses;

import com.quiroz.mypayments.enums.Month;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class PersonalFinanceResponseDto {

    private Long id;
    private int year;
    private Month month;
/*    private List<ExpenseResponseDto> expenses;
    private List<IncomeResponseDto> incomes;
    private TotalResponseDto totals;*/
}
