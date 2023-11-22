package com.quiroz.mypayments.factories;

import com.quiroz.mypayments.dto.requests.AddExpenseRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateExpenseRequestDto;
import com.quiroz.mypayments.entities.Expense;
import com.quiroz.mypayments.enums.Currency;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExpenseFactory {

    public static final Long ID = 1L;
    public static final String NOTE = "SOME NOTE";
    public static final BigDecimal AMOUNT = new BigDecimal("2.4");
    public static final Currency CURRENCY = Currency.BOB;
    public static final LocalDate EXPENSED_DATE = LocalDate.now();

    public static AddExpenseRequestDto createAddExpenseRequestDto() {
        return AddExpenseRequestDto.builder()
            .categoryId(CategoryFactory.CATEGORY_ID)
            .subcategoryId(CategoryFactory.PARENT_ID)
            .note(NOTE)
            .amount(AMOUNT)
            .currency(CURRENCY)
            .expensedDate(EXPENSED_DATE)
            .build();
    }

    public static UpdateExpenseRequestDto createUpdateExpenseRequestDto() {
        return UpdateExpenseRequestDto.builder()
            .id(ID)
            .categoryId(CategoryFactory.CATEGORY_ID)
            .subcategoryId(CategoryFactory.PARENT_ID)
            .note(NOTE)
            .amount(AMOUNT)
            .currency(CURRENCY)
            .expensedDate(EXPENSED_DATE)
            .build();
    }

    public static Expense createExpense() {
        return Expense.builder()
            .id(ID)
            .note(NOTE)
            .amount(AMOUNT)
            .currency(CURRENCY)
            .expensedDate(EXPENSED_DATE)
            .category(CategoryFactory.createCategory())
            .subCategory(CategoryFactory.createSubcategory())
            .personalFinance(PersonalFinanceFactory.createPersonalFinance())
            .build();
    }
}
