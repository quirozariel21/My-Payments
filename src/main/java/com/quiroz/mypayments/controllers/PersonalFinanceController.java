package com.quiroz.mypayments.controllers;

import com.quiroz.mypayments.dto.requests.AddPersonalFinanceRequestDto;
import com.quiroz.mypayments.dto.requests.UpdatePersonalFinanceRequestDto;
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
    public ResponseEntity<PersonalFinanceResponseDto> save(@Valid @RequestBody
                                                           AddPersonalFinanceRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(personalFinanceService.savePersonalFinance(requestDto));
    }

    @PatchMapping
    public ResponseEntity<PersonalFinanceResponseDto> update(@Valid @RequestBody
                                                             UpdatePersonalFinanceRequestDto requestDto) {
        return ResponseEntity.ok(personalFinanceService.updatePersonalFinance(requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personalFinanceService.deletePersonalFinance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalFinanceResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(personalFinanceService.getById(id));
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
