package com.quiroz.mypayments.mappers;

import com.quiroz.mypayments.dto.requests.AddIncomeRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateIncomeRequestDto;
import com.quiroz.mypayments.dto.responses.IncomeResponseDto;
import com.quiroz.mypayments.entities.Income;
import com.quiroz.mypayments.entities.PersonalFinance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IncomeMapper {


    @Mapping(target = "personalFinance.id", source = "personalFinanceId")
    Income toIncome(AddIncomeRequestDto requestDto);

    @Mapping(target = "personalFinance.id", source = "personalFinanceId")
    Income fromUpdateIncomeRequestDtoToIncome(UpdateIncomeRequestDto requestDto);

    @Mapping(target = "id", source = "income.id")
    @Mapping(target = "personalFinance.id", source = "personalFinance.id")
    @Mapping(target = "personalFinance.year", source = "personalFinance.year")
    @Mapping(target = "personalFinance.month", source = "personalFinance.month")
    IncomeResponseDto toIncomeResponseDto(Income income, PersonalFinance personalFinance);
}
