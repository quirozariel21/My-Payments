package com.quiroz.mypayments.mappers;

import com.quiroz.mypayments.dto.requests.AddExpenseRequestDto;
import com.quiroz.mypayments.dto.responses.ExpenseResponseDto;
import com.quiroz.mypayments.entities.Expense;
import com.quiroz.mypayments.entities.PersonalFinance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(source = "personalFinance.id", target = "personalFinance.id")
    @Mapping(source = "requestDto.categoryId", target = "category.id")
    @Mapping(source = "requestDto.subcategoryId", target = "subCategory.id")
    Expense toAddExpenseRequestDto(AddExpenseRequestDto requestDto,
                                   PersonalFinance personalFinance);

    @Mapping(source = "category.id", target = "category.id")
    ExpenseResponseDto toExpenseResponseDto(Expense expense);
}
