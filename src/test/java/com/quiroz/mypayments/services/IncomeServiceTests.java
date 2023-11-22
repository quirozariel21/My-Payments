package com.quiroz.mypayments.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.quiroz.mypayments.dto.requests.AddIncomeRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateIncomeRequestDto;
import com.quiroz.mypayments.dto.responses.IncomeResponseDto;
import com.quiroz.mypayments.entities.Income;
import com.quiroz.mypayments.entities.PersonalFinance;
import com.quiroz.mypayments.exception.NotFoundException;
import com.quiroz.mypayments.factories.IncomeFactory;
import com.quiroz.mypayments.factories.PersonalFinanceFactory;
import com.quiroz.mypayments.mappers.IncomeMapper;
import com.quiroz.mypayments.repositories.IncomeRepository;
import com.quiroz.mypayments.repositories.PersonalFinanceRepository;
import com.quiroz.mypayments.services.impl.IncomeServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IncomeServiceTests {

    @InjectMocks
    private IncomeServiceImpl incomeService;
    @Mock
    private IncomeRepository incomeRepository;
    @Mock
    private PersonalFinanceRepository personalFinanceRepository;
    @Mock
    private IncomeMapper incomeMapper;

    @Test
    void saveIncome_Create() {
        AddIncomeRequestDto input = IncomeFactory.createAddIncomeRequestDto();
        PersonalFinance personalFinance = PersonalFinanceFactory.createPersonalFinance();
        Income income = IncomeFactory.createIncome();

        when(personalFinanceRepository.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(personalFinance));
        when(incomeMapper.toIncome(input))
            .thenReturn(income);
        when(incomeMapper.toIncomeResponseDto(income, personalFinance))
            .thenReturn(mock(IncomeResponseDto.class));

        incomeService.addIncome(input);

        verify(incomeRepository, times(1))
            .save(Mockito.any());
    }

    @Test
    void saveIncome_ThrowsNotFoundException() {
        AddIncomeRequestDto input = IncomeFactory.createAddIncomeRequestDto();

        when(personalFinanceRepository.findById(Mockito.anyLong()))
            .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
            () -> incomeService.addIncome(input));
        verify(incomeRepository, times(0))
            .save(Mockito.any());
    }


    @Test
    void updateIncome_Update() {
        UpdateIncomeRequestDto input = IncomeFactory.createUpdateIncomeRequestDto();
        Income income = IncomeFactory.createIncome();
        when(incomeRepository.findByIdAndPersonalFinanceId(PersonalFinanceFactory.ID, input.getId()))
            .thenReturn(Optional.of(income));

        when(incomeMapper.fromUpdateIncomeRequestDtoToIncome(input))
            .thenReturn(income);
        when(incomeMapper.toIncomeResponseDto(income, income.getPersonalFinance()))
            .thenReturn(mock(IncomeResponseDto.class));

        incomeService.updateIncome(PersonalFinanceFactory.ID, input);

        verify(incomeRepository, times(1))
            .save(Mockito.any());
    }

    @Test
    void updateIncome_ThrowsNotFoundException() {
        UpdateIncomeRequestDto input = IncomeFactory.createUpdateIncomeRequestDto();
        when(incomeRepository.findByIdAndPersonalFinanceId(PersonalFinanceFactory.ID, input.getId()))
            .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
            () -> incomeService.updateIncome(PersonalFinanceFactory.ID, input));
    }

    @Test
    void deleteIncome_Delete() {

        when(incomeRepository.findByIdAndPersonalFinanceId(Mockito.anyLong(), Mockito.anyLong()))
            .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
            () -> incomeService.deleteIncome(Mockito.anyLong(), Mockito.anyLong()));

        verify(incomeRepository, never()).delete(Mockito.any());
    }

    @Test
    void deleteIncome_ThrowsNotFoundException() {
        when(incomeRepository.findByIdAndPersonalFinanceId(Mockito.anyLong(), Mockito.anyLong()))
            .thenReturn(Optional.of(mock(Income.class)));

        incomeService.deleteIncome(Mockito.anyLong(), Mockito.anyLong());
        verify(incomeRepository, atLeastOnce()).delete(Mockito.any());
    }

}
