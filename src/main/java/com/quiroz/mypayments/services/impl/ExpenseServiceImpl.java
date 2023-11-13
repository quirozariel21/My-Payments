package com.quiroz.mypayments.services.impl;

import com.quiroz.mypayments.dto.requests.AddExpenseRequestDto;
import com.quiroz.mypayments.dto.responses.ExpenseResponseDto;
import com.quiroz.mypayments.entities.Expense;
import com.quiroz.mypayments.entities.PersonalFinance;
import com.quiroz.mypayments.exception.NotFoundException;
import com.quiroz.mypayments.mappers.ExpenseMapper;
import com.quiroz.mypayments.repositories.CategoryRepository;
import com.quiroz.mypayments.repositories.ExpenseRepository;
import com.quiroz.mypayments.repositories.PersonalFinanceRepository;
import com.quiroz.mypayments.services.BaseService;
import com.quiroz.mypayments.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl extends BaseService<Expense, Long, ExpenseRepository>
        implements ExpenseService {

    private final CategoryRepository categoryRepository;
    private final PersonalFinanceRepository personalFinanceRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public ExpenseResponseDto save(AddExpenseRequestDto expenseDto) {

        log.info("Saving expense with CategoryId: {}", expenseDto.getCategoryId());
        Optional<PersonalFinance> personalFinance = personalFinanceRepository.findById(expenseDto.getPersonalFinanceId());
        if (personalFinance.isEmpty()) {
            throw new NotFoundException("PersonalFinanceId: " + expenseDto.getPersonalFinanceId() + " not found.");
        }

        var category = categoryRepository.findById(expenseDto.getCategoryId());
        if (category.isEmpty()) {
            throw new NotFoundException("CategoryId: " + expenseDto.getCategoryId() + " not found");
        }

        var subcategory = categoryRepository.findById(expenseDto.getSubcategoryId());
        if (subcategory.isEmpty()) {
            throw new NotFoundException("SubcategoryId: " + expenseDto.getSubcategoryId() + " not found");
        }

        Expense expense = expenseMapper.toAddExpenseRequestDto(expenseDto, personalFinance.get());
        repositorio.save(expense);

        return expenseMapper.toExpenseResponseDto(expense);
    }

    @Override
    public BigDecimal sumTotalSpentByPersonalFinanceId(Long personalFinanceId) {
        return repositorio.sumTotalSpentByPersonalFinanceId(personalFinanceId);
    }
}
