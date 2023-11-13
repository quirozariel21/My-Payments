package com.quiroz.mypayments.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorDto {
    @Builder.Default
    private ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.systemDefault());
    @NotNull
    private Integer status;
    @NotNull
    private String message;
    private List<ValidationErrorDto> validationErrors;
}
