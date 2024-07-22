package com.quiroz.mypayments.dto.responses;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TotalResponseDto {
    private BigDecimal totalReceived;
    private BigDecimal totalSpent;
    private BigDecimal totalSaved;
}
