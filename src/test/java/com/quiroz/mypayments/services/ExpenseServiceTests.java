package com.quiroz.mypayments.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.quiroz.mypayments.dto.requests.AddExpenseRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateExpenseRequestDto;
import com.quiroz.mypayments.dto.responses.ExpenseResponseDto;
import com.quiroz.mypayments.entities.Category;
import com.quiroz.mypayments.entities.Expense;
import com.quiroz.mypayments.entities.PersonalFinance;
import com.quiroz.mypayments.exception.NotFoundException;
import com.quiroz.mypayments.factories.ExpenseFactory;
import com.quiroz.mypayments.factories.PersonalFinanceFactory;
import com.quiroz.mypayments.mappers.ExpenseMapper;
import com.quiroz.mypayments.repositories.CategoryRepository;
import com.quiroz.mypayments.repositories.ExpenseRepository;
import com.quiroz.mypayments.repositories.PersonalFinanceRepository;
import com.quiroz.mypayments.services.impl.ExpenseServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTests {

    @InjectMocks
    private ExpenseServiceImpl expenseService;
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private PersonalFinanceRepository personalFinanceRepository;
    @Mock
    private ExpenseMapper expenseMapper;

    @Test
    void save_ThrowsPersonalFinanceNotFoundException() {
        AddExpenseRequestDto input = ExpenseFactory.createAddExpenseRequestDto();
        when(personalFinanceRepository.findById(Mockito.anyLong()))
            .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
            () -> expenseService.save(input));

        verify(expenseRepository, never()).save(Mockito.any());
    }

    @Test
    void save_Create() {
        AddExpenseRequestDto input = ExpenseFactory.createAddExpenseRequestDto();
        PersonalFinance personalFinance = PersonalFinanceFactory.createPersonalFinance();
        Expense expense = ExpenseFactory.createExpense();
        when(personalFinanceRepository.findById(Mockito.eq(input.getPersonalFinanceId())))
            .thenReturn(Optional.of(personalFinance));
        when(categoryRepository.findById(Mockito.eq(input.getCategoryId())))
            .thenReturn(Optional.of(mock(Category.class)));
        when(categoryRepository.findById(Mockito.eq(input.getSubcategoryId())))
            .thenReturn(Optional.of(mock(Category.class)));
        when(expenseMapper.toAddExpenseRequestDto(input, personalFinance))
            .thenReturn(expense);
        expenseService.save(input);

        verify(expenseRepository, atLeastOnce()).save(expense);
        verify(categoryRepository, atLeast(2))
            .findById(Mockito.anyLong());
    }

    @Test
    void update_ThrowsPersonalFinanceNotFoundException() {
        UpdateExpenseRequestDto input = ExpenseFactory.createUpdateExpenseRequestDto();
        when(expenseRepository.findByIdAndPersonalFinanceId(Mockito.eq(input.getId()), Mockito.eq(input.getPersonalFinanceId())))
            .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
            () -> expenseService.update(input));

        verify(expenseRepository, never()).save(Mockito.any());
    }

    @Test
    void update_Update() {
        UpdateExpenseRequestDto input = ExpenseFactory.createUpdateExpenseRequestDto();
        Expense expense = mock(Expense.class);

        when(expenseRepository.findByIdAndPersonalFinanceId(Mockito.eq(input.getId()), Mockito.eq(input.getPersonalFinanceId())))
            .thenReturn(Optional.of(mock(Expense.class)));

        when(categoryRepository.findById(Mockito.eq(input.getCategoryId())))
            .thenReturn(Optional.of(mock(Category.class)));
        when(categoryRepository.findById(Mockito.eq(input.getSubcategoryId())))
            .thenReturn(Optional.of(mock(Category.class)));

        when(expenseMapper.toUpdateExpenseRequestDto(input))
            .thenReturn(expense);

        when(expenseMapper.toExpenseResponseDto(expense))
            .thenReturn(mock(ExpenseResponseDto.class));

        expenseService.update(input);

        verify(expenseRepository, atLeastOnce()).save(expense);
        verify(categoryRepository, atLeast(2))
            .findById(Mockito.anyLong());
    }

    @Test
    void delete_ThrowsNotFoundException() {
        when(expenseRepository.findById(Mockito.anyLong()))
            .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
            () -> expenseService.delete(Mockito.anyLong()));

        verify(expenseRepository, never()).delete(Mockito.any());
    }

    @Test
    void delete_Delete() {
        Expense expense = mock(Expense.class);
        when(expenseRepository.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(expense));

        expenseService.delete(Mockito.anyLong());

        verify(expenseRepository, atLeastOnce()).delete(Mockito.any());
    }
}
