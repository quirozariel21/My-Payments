package com.quiroz.mypayments.dto.responses;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {

    private long id;
    private String code;
    private String name;
    private String description;
}
