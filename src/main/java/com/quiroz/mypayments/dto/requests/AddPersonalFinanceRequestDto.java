package com.quiroz.mypayments.dto.requests;

import com.quiroz.mypayments.enums.Month;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddPersonalFinanceRequestDto {
    @NotNull
    private int year;
    @NotNull
    private Month month;
}
