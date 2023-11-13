package com.quiroz.mypayments.services.impl;

import com.quiroz.mypayments.dto.requests.AddIncomeRequestDto;
import com.quiroz.mypayments.dto.responses.IncomeResponseDto;
import com.quiroz.mypayments.entities.Income;
import com.quiroz.mypayments.entities.PersonalFinance;
import com.quiroz.mypayments.exception.NotFoundException;
import com.quiroz.mypayments.mappers.IncomeMapper;
import com.quiroz.mypayments.repositories.IncomeRepository;
import com.quiroz.mypayments.repositories.PersonalFinanceRepository;
import com.quiroz.mypayments.services.BaseService;
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
public class IncomeServiceImpl extends BaseService<Income, Long, IncomeRepository>
        implements IncomeService {

    private final IncomeMapper incomeMapper;
    private final PersonalFinanceService personalFinanceService;
    private final PersonalFinanceRepository personalFinanceRepository;
    @Override
    public IncomeResponseDto addIncome(AddIncomeRequestDto requestDto) {
        log.info("Adding income: {}", requestDto.getName());
        Optional<PersonalFinance> personalFinance = personalFinanceRepository.findById(requestDto.getPersonalFinanceId());
        if (personalFinance.isEmpty()) {
            throw new NotFoundException("PersonalFinanceId: " + requestDto.getPersonalFinanceId() + " not found.");
        }

        Income income = incomeMapper.toIncome(requestDto);
        repositorio.save(income);
        log.info("{}",income.getPersonalFinance().getMonth());
        return incomeMapper.toIncomeResponseDto(income, personalFinance.get());
    }

    @Override
    public BigDecimal sumTotalReceivedByPersonalFinanceId(Long personalFinanceId) {

        return repositorio.sumTotalReceivedByPersonalFinanceId(personalFinanceId);
    }
}
