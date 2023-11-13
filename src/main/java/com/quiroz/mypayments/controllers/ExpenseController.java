package com.quiroz.mypayments.controllers;

import com.quiroz.mypayments.dto.requests.AddExpenseRequestDto;
import com.quiroz.mypayments.dto.responses.ExpenseResponseDto;
import com.quiroz.mypayments.services.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/api/v1/expense")
@RequiredArgsConstructor
@Validated
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> saveExpense(@Valid @RequestBody AddExpenseRequestDto requestDto) {
        return ResponseEntity.ok(expenseService.save(requestDto));
    }

    @GetMapping("/sumTotalSpent")
    public BigDecimal sumTotalSpent(@RequestParam Long personalFinanceId) {
        return expenseService.sumTotalSpentByPersonalFinanceId(personalFinanceId);
    }
}
