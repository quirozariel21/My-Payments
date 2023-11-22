package com.quiroz.mypayments.controllers;

import com.quiroz.mypayments.dto.requests.AddIncomeRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateIncomeRequestDto;
import com.quiroz.mypayments.dto.responses.IncomeResponseDto;
import com.quiroz.mypayments.services.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/api/vi/{personalFinanceId}/income")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    //TODO REFACTOR ADD personalFinanceId
    @PostMapping
    public ResponseEntity<IncomeResponseDto> saveIncome(@PathVariable Long personalFinanceId,
                                                        @Valid @RequestBody
                                                        AddIncomeRequestDto requestDto) {
        return ResponseEntity.ok(incomeService.addIncome(requestDto));
    }

    @PatchMapping
    public ResponseEntity<IncomeResponseDto> updateIncome(@PathVariable Long personalFinanceId,
                                                          @Valid @RequestBody
                                                          UpdateIncomeRequestDto requestDto) {
        return ResponseEntity.ok(incomeService.updateIncome(personalFinanceId, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long personalFinanceId,
                                             @PathVariable Long id) {
        incomeService.deleteIncome(personalFinanceId, id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/sumTotalReceived")
    public BigDecimal sumTotalReceivedByPersonalFinanceId(@RequestParam Long personalFinanceId) {
        return incomeService.sumTotalReceivedByPersonalFinanceId(personalFinanceId);
    }
}
