package com.quiroz.mypayments.controllers;

import com.quiroz.mypayments.dto.requests.AddExpenseRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateExpenseRequestDto;
import com.quiroz.mypayments.dto.responses.ExpenseResponseDto;
import com.quiroz.mypayments.services.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ExpenseResponseDto> saveExpense(@Valid @RequestBody
                                                          AddExpenseRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(expenseService.save(requestDto));
    }

    @PatchMapping
    public ResponseEntity<ExpenseResponseDto> update(@Valid @RequestBody
                                                     UpdateExpenseRequestDto requestDto) {
        return ResponseEntity.ok(expenseService.update(requestDto));
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/sumTotalSpent")
    public BigDecimal sumTotalSpent(@RequestParam Long personalFinanceId) {
        return expenseService.sumTotalSpentByPersonalFinanceId(personalFinanceId);
    }
}
