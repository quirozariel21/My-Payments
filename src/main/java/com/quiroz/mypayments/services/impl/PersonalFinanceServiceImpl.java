package com.quiroz.mypayments.services.impl;

import com.quiroz.mypayments.dto.requests.AddPersonalFinanceRequestDto;
import com.quiroz.mypayments.dto.requests.UpdatePersonalFinanceRequestDto;
import com.quiroz.mypayments.dto.responses.PersonalFinanceResponseDto;
import com.quiroz.mypayments.dto.responses.TotalResponseDto;
import com.quiroz.mypayments.entities.Expense;
import com.quiroz.mypayments.entities.Income;
import com.quiroz.mypayments.entities.PersonalFinance;
import com.quiroz.mypayments.exception.NotFoundException;
import com.quiroz.mypayments.mappers.PersonalFinanceMapper;
import com.quiroz.mypayments.repositories.PersonalFinanceRepository;
import com.quiroz.mypayments.services.ExpenseService;
import com.quiroz.mypayments.services.PersonalFinanceService;
import jakarta.persistence.EntityExistsException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalFinanceServiceImpl implements PersonalFinanceService {

    private final ExpenseService expenseService;
    private final PersonalFinanceMapper personalFinanceMapper;
    private final PersonalFinanceRepository personalFinanceRepository;

    @Override
    public PersonalFinanceResponseDto savePersonalFinance(AddPersonalFinanceRequestDto requestDto) {
        log.info(
                "Saving personalFinance with year: {} and month: {}",
                requestDto.getYear(),
                requestDto.getMonth());
        personalFinanceRepository
                .findByYearAndMonth(requestDto.getYear(), requestDto.getMonth())
                .orElseThrow(
                        () ->
                                new EntityExistsException(
                                        "PersonalFinance with year: "
                                                + requestDto.getYear()
                                                + " and month: "
                                                + requestDto.getMonth()
                                                + " already exist."));

        PersonalFinance personalFinance = personalFinanceMapper.toAddPersonalFinance(requestDto);
        personalFinanceRepository.save(personalFinance);
        return personalFinanceMapper.toPersonalFinanceResponseDto(personalFinance);
    }

    @Override
    public PersonalFinanceResponseDto updatePersonalFinance(
            UpdatePersonalFinanceRequestDto requestDto) {
        log.info("Deleting personalFinanceId: {}", requestDto.getId());
        var personalFinance =
                personalFinanceRepository
                        .findById(requestDto.getId())
                        .orElseThrow(
                                () ->
                                        new NotFoundException(
                                                String.format(
                                                        "personalFinanceId: %s not found",
                                                        requestDto.getId())));
        personalFinance.setYear(requestDto.getYear());
        personalFinance.setMonth(requestDto.getMonth());
        personalFinanceRepository.save(personalFinance);
        return personalFinanceMapper.toPersonalFinanceResponseDto(personalFinance);
    }

    @Override
    public void deletePersonalFinance(Long id) {
        log.info("Deleting personalFinanceId: {}", id);
        var personalFinance =
                personalFinanceRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new NotFoundException(
                                                String.format(
                                                        "personalFinanceId: %s not found", id)));
        personalFinanceRepository.delete(personalFinance);
    }

    @Override
    public PersonalFinanceResponseDto getById(Long id) {
        log.info("Getting personalFinanceId: {}", id);
        var personalFinance =
                personalFinanceRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new NotFoundException(
                                                String.format(
                                                        "personalFinanceId: %s not found", id)));
        return personalFinanceMapper.toPersonalFinanceResponseDto(personalFinance);
    }

    @Override
    public TotalResponseDto getTotals(Long personalFinanceId) {
        PersonalFinance personalFinance =
                personalFinanceRepository
                        .findById(personalFinanceId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "PersonalFinanceId: "
                                                        + personalFinanceId
                                                        + " not found."));

        var totalReceived =
                personalFinance.getIncomes().stream()
                        .map(Income::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        var totalSpent =
                personalFinance.getExpenses().stream()
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
