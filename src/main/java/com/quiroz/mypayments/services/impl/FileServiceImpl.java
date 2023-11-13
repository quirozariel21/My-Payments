package com.quiroz.mypayments.services.impl;

import com.quiroz.mypayments.dto.responses.CategoryFileResponseDto;
import com.quiroz.mypayments.dto.responses.SubcategoryFileResponseDto;
import com.quiroz.mypayments.entities.Category;
import com.quiroz.mypayments.entities.Expense;
import com.quiroz.mypayments.entities.Income;
import com.quiroz.mypayments.entities.PersonalFinance;
import com.quiroz.mypayments.enums.Currency;
import com.quiroz.mypayments.enums.Month;
import com.quiroz.mypayments.exception.NotFoundException;
import com.quiroz.mypayments.helper.HelperFile;
import com.quiroz.mypayments.models.ExpensiveModel;
import com.quiroz.mypayments.repositories.CategoryRepository;
import com.quiroz.mypayments.repositories.ExpenseRepository;
import com.quiroz.mypayments.repositories.IncomeRepository;
import com.quiroz.mypayments.repositories.PersonalFinanceRepository;
import com.quiroz.mypayments.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final PersonalFinanceRepository personalFinanceRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void loadMyPaymentsByMonth(int year, Month month, MultipartFile file) {
        log.info("[Start] - Loading MyPayments with year: {} and month: {}", year, month);

        if (!HelperFile.hasExcelFormat(file)) {
            throw new IllegalArgumentException("File is not an excel file");
        }

        //TODO review rollback if any expense fails saving into the DB
        Map<String, BigDecimal> incomeMap;
        List<ExpensiveModel> expensesToSave;
        try {
            incomeMap = HelperFile.getIncomeByMonth(month, file.getInputStream()); // TODO fix repeated values
            expensesToSave = HelperFile.getExpensesByMonth(month, file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var personalFinance = PersonalFinance.builder()
                .year(year)
                .month(month)
                .build();

        if (personalFinanceRepository.findByYearAndMonth(year, month).isPresent()) {
            throw new IllegalArgumentException("PersonalFinance with year: " + year + " and month" + month + " already exist");
        }

        personalFinanceRepository.save(personalFinance);
        log.info("PersonalFinance saved with id: {}", personalFinance.getId());

        for (Map.Entry<String, BigDecimal> stringBigDecimalEntry : incomeMap.entrySet()) {
            Income income = Income.builder()
                    .personalFinance(personalFinance)
                    .name(stringBigDecimalEntry.getKey())
                    .amount(stringBigDecimalEntry.getValue())
                    .currency(Currency.BOB)
                    .build();
            incomeRepository.save(income);
        }
        log.info("Incomes saved");

        var myExpenses = expensesToSave.stream().filter(e -> e.getId() != 0).toList();  //TODO fix in Helper class
        for (ExpensiveModel expens : myExpenses) {

            Category category = categoryRepository.findByNameAndCodeIgnoreCase(expens.getCategory(), expens.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format("Category: %s not found", expens.getCategory())));

            Category subcategory = categoryRepository.findByNameAndParentId(expens.getSubCategory(), category.getId())
                    .orElseThrow(() -> new NotFoundException(String.format("Subcategory %s and CategoryId: %s not found", expens.getSubCategory(), category.getId())));

            Expense expense = Expense.builder()
                    .personalFinance(personalFinance)
                    .category(category)
                    .subCategory(subcategory)
                    .note(expens.getDescription())
                    .amount(expens.getAmount())
                    .currency(Currency.BOB)
                    .expensedDate(LocalDate.from(expens.getDate()))
                    .build();
            expenseRepository.save(expense);
        }

    }

    @Override
    public List<CategoryFileResponseDto> loadSettings(MultipartFile file) {
        log.info("[Start] - Loading Settings...");
        if (!HelperFile.hasExcelFormat(file)) {
            throw new IllegalArgumentException("File is not an excel file");
        }

        Map<String, Set<String>> settings;
        try {
            settings = HelperFile.getSettingsFromFile(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //TODO add uni tests
        List<CategoryFileResponseDto> responseDto = new LinkedList<>();
        for (Map.Entry<String, Set<String>> stringSetEntry : settings.entrySet()) {
            String category = stringSetEntry.getKey();
            Set<String> subcategories = stringSetEntry.getValue();
            Category categoryEntity = Category.builder()
                    .code(category.toUpperCase())
                    .name(category)
                    .build();
            categoryRepository.save(categoryEntity);


            List<SubcategoryFileResponseDto> subcategoryFileResponse = new LinkedList<>();
            for (String subcategory : subcategories) {
                Category subcategoryEntity = Category.builder()
                        .name(subcategory)
                        .parentId(categoryEntity.getId())
                        .build();
                categoryRepository.save(subcategoryEntity);

                var subcategoryFile = SubcategoryFileResponseDto.builder()
                        .id(subcategoryEntity.getId())
                        .code(subcategoryEntity.getCode())
                        .name(subcategoryEntity.getName())
                        .description(subcategoryEntity.getDescription())
                        .build();
                subcategoryFileResponse.add(subcategoryFile);
            }
            var categoryFileResponse = CategoryFileResponseDto.builder()
                    .id(categoryEntity.getId())
                    .code(categoryEntity.getCode())
                    .name(categoryEntity.getName())
                    .description(categoryEntity.getDescription())
                    .subcategories(subcategoryFileResponse)
                    .build();

            responseDto.add(categoryFileResponse);
        }
        log.info("[Completed] - Settings loaded....");
        return responseDto;
    }
}
