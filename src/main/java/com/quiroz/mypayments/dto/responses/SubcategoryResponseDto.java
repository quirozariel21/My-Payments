package com.quiroz.mypayments.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubcategoryResponseDto {

    private long id;
    private String code;
    private String name;
    private String description;
    private CategoryResponseDto category;

}
