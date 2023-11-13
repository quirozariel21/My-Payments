package com.quiroz.mypayments.services.impl;

import com.quiroz.mypayments.dto.requests.AddPersonalFinanceRequestDto;
import com.quiroz.mypayments.dto.responses.PersonalFinanceResponseDto;
import com.quiroz.mypayments.dto.responses.TotalResponseDto;
import com.quiroz.mypayments.entities.Expense;
import com.quiroz.mypayments.entities.Income;
import com.quiroz.mypayments.entities.PersonalFinance;
import com.quiroz.mypayments.enums.Month;
import com.quiroz.mypayments.mappers.PersonalFinanceMapper;
import com.quiroz.mypayments.repositories.PersonalFinanceRepository;
import com.quiroz.mypayments.services.BaseService;
import com.quiroz.mypayments.services.ExpenseService;
import com.quiroz.mypayments.services.PersonalFinanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalFinanceServiceImpl extends BaseService<PersonalFinance, Long, PersonalFinanceRepository>
        implements PersonalFinanceService {

    private final ExpenseService expenseService;
    private final PersonalFinanceMapper personalFinanceMapper;

    @Override
    public PersonalFinanceResponseDto savePersonalFinance(AddPersonalFinanceRequestDto requestDto) {
        log.info("Saving personalFinance with year: {} and month: {}", requestDto.getYear(), requestDto.getMonth());
        var personalFinanceFound = repositorio.findByYearAndMonth(requestDto.getYear(), requestDto.getMonth());
        if (personalFinanceFound.isPresent()) {
            throw  new IllegalArgumentException("PersonalFinance with year: " + requestDto.getYear() + " and month: " + requestDto.getMonth() + " already exist.");
        }

        PersonalFinance personalFinance = personalFinanceMapper.toAddPersonalFinance(requestDto);
        repositorio.save(personalFinance);
        return personalFinanceMapper.toPersonalFinanceResponseDto(personalFinance);
    }

    @Override
    public TotalResponseDto getTotals(Long personalFinanceId) {
        PersonalFinance personalFinance = repositorio.findById(personalFinanceId)
                .orElseThrow(() -> new IllegalArgumentException("PersonalFinanceId: " + personalFinanceId + " not found."));

        var totalReceived = personalFinance.getIncomes()
                .stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var totalSpent = personalFinance.getExpenses()
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSaved = totalReceived.subtract(totalSpent);

        return TotalResponseDto.builder()
                .totalReceived(totalReceived)
                .totalSpent(totalSpent)
                .totalSaved(totalSaved)
                .build();

    }

}
