package com.quiroz.mypayments.dto.responses;

import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFileResponseDto {

    private long id;
    private String code;
    private String name;
    private String description;
    private List<SubcategoryFileResponseDto> subcategories;
}
