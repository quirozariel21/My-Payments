package com.quiroz.mypayments.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldNameConstants //TODO research
public class ValidationErrorDto {
    @NotNull
    private String field;
    @NotNull
    private String message;
    @NotNull
    private String code;
    private String rejectedValue;
}
