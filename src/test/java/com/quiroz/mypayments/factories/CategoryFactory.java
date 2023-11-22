package com.quiroz.mypayments.factories;

import com.quiroz.mypayments.dto.requests.AddCategoryRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateCategoryRequestDto;
import com.quiroz.mypayments.entities.Category;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryFactory {

    public static final Long CATEGORY_ID = 1L;
    public static final String CATEGORY_NAME = "TEST";
    public static final String CATEGORY_CODE = "TEST";
    public static final String CATEGORY_DESCRIPTION = "SOME DESCRIPTION";
    public static final Long PARENT_ID = 2L;

    public static final Long SUBCATEGORY_ID = 1L;
    public static final String SUBCATEGORY_NAME = "TEST";
    public static final String SUBCATEGORY_CODE = "TEST";
    public static final String SUBCATEGORY_DESCRIPTION = "SOME DESCRIPTION";


    public static AddCategoryRequestDto createAddCategoryRequestDtoMock() {
        return AddCategoryRequestDto.builder()
            .code(CATEGORY_CODE)
            .name(CATEGORY_NAME)
            .description(CATEGORY_DESCRIPTION)
            .build();
    }

    public static AddCategoryRequestDto createAddCategoryRequestDtoWithNullValuesMock() {
        return AddCategoryRequestDto.builder()
            .description(CATEGORY_DESCRIPTION)
            .build();
    }

    public static UpdateCategoryRequestDto createUpdateCategoryRequestDtoMock() {
        return UpdateCategoryRequestDto.builder()
            .id(CATEGORY_ID)
            .code(CATEGORY_CODE)
            .name(CATEGORY_NAME)
            .description(CATEGORY_DESCRIPTION)
            .build();
    }

    public static Category createCategory() {
        return Category.builder()
            .id(CATEGORY_ID)
            .code(CATEGORY_CODE)
            .name(CATEGORY_NAME)
            .description(CATEGORY_DESCRIPTION)
            .build();
    }

    public static Category createSubcategory() {
        return Category.builder()
            .id(SUBCATEGORY_ID)
            .code(SUBCATEGORY_CODE)
            .name(SUBCATEGORY_NAME)
            .description(SUBCATEGORY_DESCRIPTION)
            .parentId(PARENT_ID)
            .build();
    }
}
