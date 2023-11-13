package com.quiroz.mypayments.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class TotalResponseDto {
    private BigDecimal totalReceived;
    private BigDecimal totalSpent;
    private BigDecimal totalSaved;

}
