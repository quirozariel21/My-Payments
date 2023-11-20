package com.quiroz.mypayments.services;

import com.quiroz.mypayments.dto.requests.AddCategoryRequestDto;
import com.quiroz.mypayments.dto.requests.AddSubcategoryRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateCategoryRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateSubcategoryRequestDto;
import com.quiroz.mypayments.dto.responses.CategoryResponseDto;
import com.quiroz.mypayments.dto.responses.SubcategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto saveCategory(AddCategoryRequestDto addCategoryRequestDto);
    CategoryResponseDto updateCategory(UpdateCategoryRequestDto categoryRequestDto);
    void deleteCategory(Long id);

    List<CategoryResponseDto> getAllCategories(int page, int size, String[] sort);

    CategoryResponseDto getCategoryById(Long id);

    List<SubcategoryResponseDto> saveSubcategories(Long categoryId, List<AddSubcategoryRequestDto> subcategoriesRequest);

    SubcategoryResponseDto saveSubcategory(Long categoryId, AddSubcategoryRequestDto subcategoryRequest);

    SubcategoryResponseDto updateSubcategory(UpdateSubcategoryRequestDto requestDto);

    void deleteSubcategory(Long id);

    SubcategoryResponseDto getSubcategoryById(Long id);
}
