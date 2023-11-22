package com.quiroz.mypayments.services.impl;

import com.quiroz.mypayments.dto.requests.AddIncomeRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateIncomeRequestDto;
import com.quiroz.mypayments.dto.responses.IncomeResponseDto;
import com.quiroz.mypayments.entities.Income;
import com.quiroz.mypayments.entities.PersonalFinance;
import com.quiroz.mypayments.exception.NotFoundException;
import com.quiroz.mypayments.mappers.IncomeMapper;
import com.quiroz.mypayments.repositories.IncomeRepository;
import com.quiroz.mypayments.repositories.PersonalFinanceRepository;
import com.quiroz.mypayments.services.IncomeService;
import com.quiroz.mypayments.services.PersonalFinanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncomeServiceImpl implements IncomeService {

    private final IncomeMapper incomeMapper;
    private final IncomeRepository incomeRepository;
    private final PersonalFinanceRepository personalFinanceRepository;
    @Override
    public IncomeResponseDto addIncome(AddIncomeRequestDto requestDto) {
        log.info("Adding income: {}", requestDto.getName());
        Optional<PersonalFinance> personalFinance = personalFinanceRepository.findById(requestDto.getPersonalFinanceId());
        if (personalFinance.isEmpty()) {
            throw new NotFoundException("PersonalFinanceId: " + requestDto.getPersonalFinanceId() + " not found.");
        }

        Income income = incomeMapper.toIncome(requestDto);
        incomeRepository.save(income);

        return incomeMapper.toIncomeResponseDto(income, personalFinance.get());
    }

    @Override
    public IncomeResponseDto updateIncome(Long personalFinanceId, UpdateIncomeRequestDto requestDto) {
        log.info("updating incomeId: {} and personalFinanceId: {}", requestDto.getId(), personalFinanceId);
        requestDto.setPersonalFinanceId(personalFinanceId);
        Income income = incomeRepository.findByIdAndPersonalFinanceId(personalFinanceId,
                requestDto.getId()).orElseThrow(() -> new NotFoundException(String.format("Income: %s and personalFinanceId: %s not found", requestDto.getId(), personalFinanceId)));

        PersonalFinance personalFinance = income.getPersonalFinance();

        Income incomeToUpdate = incomeMapper.fromUpdateIncomeRequestDtoToIncome(requestDto);
        incomeRepository.save(incomeToUpdate);

        return incomeMapper.toIncomeResponseDto(incomeToUpdate, personalFinance);
    }

    @Override
    public void deleteIncome(Long personalFinanceId, Long incomeId) {
        log.info("Deleting incomeId: {} and personalFinanceId: {}", incomeId, personalFinanceId);
        Income income = incomeRepository.findByIdAndPersonalFinanceId(personalFinanceId,
            incomeId).orElseThrow(() ->
            new NotFoundException(String.format("Income: %s and personalFinanceId: %s not found", incomeId, personalFinanceId)));

        incomeRepository.delete(income);
    }

    @Override
    public BigDecimal sumTotalReceivedByPersonalFinanceId(Long personalFinanceId) {

        return incomeRepository.sumTotalReceivedByPersonalFinanceId(personalFinanceId);
    }
}
