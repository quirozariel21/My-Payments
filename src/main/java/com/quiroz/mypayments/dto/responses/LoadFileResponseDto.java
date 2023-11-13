package com.quiroz.mypayments.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoadFileResponseDto {
    private Long startTime;
    private Long endTime;
    private Long executionTime;
}
