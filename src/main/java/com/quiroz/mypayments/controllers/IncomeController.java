package com.quiroz.mypayments.controllers;

import com.quiroz.mypayments.dto.requests.AddIncomeRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateIncomeRequestDto;
import com.quiroz.mypayments.dto.responses.IncomeResponseDto;
import com.quiroz.mypayments.services.IncomeService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/vi/income")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeResponseDto> saveIncome(
            @Valid @RequestBody AddIncomeRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incomeService.addIncome(requestDto));
    }

    @PatchMapping
    public ResponseEntity<IncomeResponseDto> updateIncome(
            @Valid @RequestBody UpdateIncomeRequestDto requestDto) {
        return ResponseEntity.ok(incomeService.updateIncome(requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sumTotalReceived")
    public BigDecimal sumTotalReceivedByPersonalFinanceId(@RequestParam Long personalFinanceId) {
        return incomeService.sumTotalReceivedByPersonalFinanceId(personalFinanceId);
    }
}
