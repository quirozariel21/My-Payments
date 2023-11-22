package com.quiroz.mypayments.services.impl;

import com.quiroz.mypayments.dto.requests.AddExpenseRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateExpenseRequestDto;
import com.quiroz.mypayments.dto.responses.ExpenseResponseDto;
import com.quiroz.mypayments.entities.Expense;
import com.quiroz.mypayments.entities.PersonalFinance;
import com.quiroz.mypayments.exception.NotFoundException;
import com.quiroz.mypayments.mappers.ExpenseMapper;
import com.quiroz.mypayments.repositories.CategoryRepository;
import com.quiroz.mypayments.repositories.ExpenseRepository;
import com.quiroz.mypayments.repositories.PersonalFinanceRepository;
import com.quiroz.mypayments.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    private final CategoryRepository categoryRepository;
    private final PersonalFinanceRepository personalFinanceRepository;
    private final ExpenseMapper expenseMapper;
    private final ExpenseRepository expenseRepository;

    @Override
    public ExpenseResponseDto save(AddExpenseRequestDto expenseDto) {
        log.info("Saving expense");
        PersonalFinance personalFinance = personalFinanceRepository.findById(expenseDto.getPersonalFinanceId())
            .orElseThrow(() -> new NotFoundException("PersonalFinanceId: " + expenseDto.getPersonalFinanceId() + " not found."));

        categoryRepository.findById(expenseDto.getCategoryId())
            .orElseThrow(() -> new NotFoundException("CategoryId: " + expenseDto.getCategoryId() + " not found"));

        categoryRepository.findById(expenseDto.getSubcategoryId())
            .orElseThrow(() -> new NotFoundException("SubcategoryId: " + expenseDto.getSubcategoryId() + " not found"));

        Expense expense = expenseMapper.toAddExpenseRequestDto(expenseDto, personalFinance);
        expenseRepository.save(expense);

        return expenseMapper.toExpenseResponseDto(expense);
    }

    @Override
    public ExpenseResponseDto update(UpdateExpenseRequestDto requestDto) {
        log.info("updating expenseId: {} and personalFinanceId: {}", requestDto.getId(), requestDto.getPersonalFinanceId());
        expenseRepository.findByIdAndPersonalFinanceId(requestDto.getId(), requestDto.getPersonalFinanceId())
            .orElseThrow(() -> new NotFoundException(String.format("expenseId: %s and personalFinanceId: %s not found",
                requestDto.getId(), requestDto.getPersonalFinanceId())));

        categoryRepository.findById(requestDto.getCategoryId())
            .orElseThrow(() -> new NotFoundException("CategoryId: " + requestDto.getCategoryId() + " not found"));

        categoryRepository.findById(requestDto.getSubcategoryId())
            .orElseThrow(() -> new NotFoundException("SubcategoryId: " + requestDto.getSubcategoryId() + " not found"));

        Expense expenseToUpdate = expenseMapper.toUpdateExpenseRequestDto(requestDto, requestDto.getPersonalFinanceId());
        expenseRepository.save(expenseToUpdate);

        return expenseMapper.toExpenseResponseDto(expenseToUpdate);
    }

    @Override
    public void delete(Long expenseId) {
        log.info("updating expenseId: {}", expenseId);
        Expense expense = expenseRepository.findById(expenseId)
            .orElseThrow(() -> new NotFoundException(String.format("expenseId: %s not found", expenseId)));

        expenseRepository.delete(expense);
    }

    @Override
    public BigDecimal sumTotalSpentByPersonalFinanceId(Long personalFinanceId) {
        return expenseRepository.sumTotalSpentByPersonalFinanceId(personalFinanceId);
    }
}
