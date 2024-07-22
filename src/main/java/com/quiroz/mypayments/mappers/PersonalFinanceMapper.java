package com.quiroz.mypayments.mappers;

import com.quiroz.mypayments.dto.requests.AddPersonalFinanceRequestDto;
import com.quiroz.mypayments.dto.responses.PersonalFinanceResponseDto;
import com.quiroz.mypayments.entities.PersonalFinance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonalFinanceMapper {

    // @Mapping(target = "createdAt", defaultValue = "LocalDateTime.now()")
    @Mapping(ignore = true, target = "createdAt")
    PersonalFinance toAddPersonalFinance(AddPersonalFinanceRequestDto requestDto);

    PersonalFinanceResponseDto toPersonalFinanceResponseDto(PersonalFinance personalFinance);

    /*    public PersonalFinanceResponseDto convertToPersonalFinanceResponseDto(PersonalFinance personalFinance) {
        AtomicInteger number = new AtomicInteger();
        LinkedList<ExpenseResponseDto> expenses = new LinkedList<>();
        personalFinance.getExpenses().forEach(expense -> {
            ExpenseResponseDto expenseResponseDto = ExpenseResponseDto.builder()
                    .id(expense.getId())
                    .number(number.getAndIncrement())
                    .amount(expense.getAmount())
                    .currency(expense.getCurrency())
                    .note(expense.getNote())
                    .build();
            expenses.add(expenseResponseDto);
        });

        List<IncomeResponseDto> incomes = personalFinance.getIncomes()
                .stream().map(income -> IncomeResponseDto.builder()
                        .id(income.getId())
                        .name(income.getName())
                        .amount(income.getAmount())
                        .currency(income.getCurrency())
                        .build())
                .toList();

        var totalReceived = personalFinance.getIncomes()
                .stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var totalSpent = personalFinance.getExpenses()
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSaved = totalReceived.subtract(totalSpent);

       var totalResponse = TotalResponseDto.builder()
                .totalReceived(totalReceived)
                .totalSpent(totalSpent)
                .totalSaved(totalSaved)
                .build();

        return PersonalFinanceResponseDto.builder()
                .id(personalFinance.getId())
                .year(personalFinance.getYear())
                .month(personalFinance.getMonth())
                .incomes(incomes)
                .expenses(expenses)
                .totals(totalResponse)
                .build();

    }*/
}
