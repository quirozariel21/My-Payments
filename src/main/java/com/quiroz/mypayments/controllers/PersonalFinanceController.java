package com.quiroz.mypayments.controllers;

import com.quiroz.mypayments.dto.requests.AddPersonalFinanceRequestDto;
import com.quiroz.mypayments.dto.responses.PersonalFinanceResponseDto;
import com.quiroz.mypayments.dto.responses.TotalResponseDto;
import com.quiroz.mypayments.enums.Month;
import com.quiroz.mypayments.services.PersonalFinanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/personalFinance")
@RequiredArgsConstructor
public class PersonalFinanceController {

    private final PersonalFinanceService personalFinanceService;

    @PostMapping
    public PersonalFinanceResponseDto save(@Valid @RequestBody AddPersonalFinanceRequestDto requestDto){
        return personalFinanceService.savePersonalFinance(requestDto);
    }


    @GetMapping("/getTotals")
    public ResponseEntity<TotalResponseDto> getTotals(@RequestParam Long personalFinanceId) {
        var response = personalFinanceService.getTotals(personalFinanceId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/get-total-expenses")
    public ResponseEntity<Void> getTotalExpensesByYearAndMonth(@PathVariable Long id,
                                                               @RequestParam int year,
                                                               @RequestParam Month month) {
        return ResponseEntity.ok(null);
    }

}
