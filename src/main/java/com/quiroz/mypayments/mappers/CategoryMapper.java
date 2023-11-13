package com.quiroz.mypayments.mappers;

import com.quiroz.mypayments.dto.requests.AddCategoryRequestDto;
import com.quiroz.mypayments.dto.responses.CategoryResponseDto;
import com.quiroz.mypayments.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {


    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "parentId")
    @Mapping(ignore = true, target = "createdAt")
    Category toAddCategoryRequestDto(AddCategoryRequestDto categoryRequest);

    CategoryResponseDto toCategoryResponseDto(Category category);


  /*  public List<CategoryResponseDto> convertToCategoryResponseDtoList(List<Category> categories) {

        var categoriesResponse = categories.stream()
                .filter(category -> null == category.getCategoryId())
                .map(category ->
                        CategoryResponseDto.builder()
                                .id(category.getId())
                                .code(category.getCode())
                                .name(category.getName())
                                .description(category.getDescription())
                                .build())
                .toList();

        categoriesResponse.stream().forEach(categoryResponseDto -> {
            var subcategories = categories.stream()
                    .filter(category -> null != category.getCategoryId() &&
                            category.getCategoryId().equals(categoryResponseDto.getId()))
                    .map(category -> SubcategoryResponseDto.builder()
                            .id(category.getId())
                            .code(category.getCode())
                            .name(category.getName())
                            .description(category.getDescription())
                            .build()).toList();
            //categoryResponseDto.setSubcategory(subcategories);
        });

        return categoriesResponse;
    }
*/

}
