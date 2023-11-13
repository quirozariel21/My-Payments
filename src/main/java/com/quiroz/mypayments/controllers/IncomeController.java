package com.quiroz.mypayments.controllers;

import com.quiroz.mypayments.dto.requests.AddIncomeRequestDto;
import com.quiroz.mypayments.dto.responses.IncomeResponseDto;
import com.quiroz.mypayments.services.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/api/vi/income")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeResponseDto> saveIncome(@Valid @RequestBody AddIncomeRequestDto requestDto) {
        return ResponseEntity.ok(incomeService.addIncome(requestDto));
    }

    @GetMapping("/sumTotalReceived")
    public BigDecimal sumTotalReceivedByPersonalFinanceId(@RequestParam Long personalFinanceId) {
        return incomeService.sumTotalReceivedByPersonalFinanceId(personalFinanceId);
    }
}
