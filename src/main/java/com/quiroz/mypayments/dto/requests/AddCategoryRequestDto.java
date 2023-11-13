package com.quiroz.mypayments.dto.requests;

import com.quiroz.mypayments.validations.NullOrNotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCategoryRequestDto {
    @NullOrNotBlank
    @Size(min = 3, message = "Code should have at least 3 characters")
    private String code;
    @NullOrNotBlank
    private String name;
    private String description;
}
