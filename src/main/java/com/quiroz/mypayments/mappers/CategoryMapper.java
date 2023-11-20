package com.quiroz.mypayments.mappers;

import com.quiroz.mypayments.dto.requests.AddCategoryRequestDto;
import com.quiroz.mypayments.dto.requests.AddSubcategoryRequestDto;
import com.quiroz.mypayments.dto.responses.CategoryResponseDto;
import com.quiroz.mypayments.dto.responses.SubcategoryResponseDto;
import com.quiroz.mypayments.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {


    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "parentId")
    @Mapping(ignore = true, target = "createdAt")
    Category fromAddCategoryRequestDtoToCategory(AddCategoryRequestDto categoryRequest);

    CategoryResponseDto toCategoryResponseDto(Category category);


    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "createdAt")
    Category fromAddSubcategoryRequestDtoToCategory(AddSubcategoryRequestDto categoryRequest);

    @Mapping(source = "subcategory.id", target = "id")
    @Mapping(source = "subcategory.code", target = "code")
    @Mapping(source = "subcategory.name", target = "name")
    @Mapping(source = "subcategory.description", target = "description")
    @Mapping(source = "parentCategory.id", target = "category.id")
    @Mapping(source = "parentCategory.code", target = "category.code")
    @Mapping(source = "parentCategory.name", target = "category.name")
    @Mapping(source = "parentCategory.description", target = "category.description")
    SubcategoryResponseDto fromCategoryToSubcategoryResponseDto(Category subcategory,
                                                                Category parentCategory);

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
