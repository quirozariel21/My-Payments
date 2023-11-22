package com.quiroz.mypayments.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.quiroz.mypayments.dto.requests.AddCategoryRequestDto;
import com.quiroz.mypayments.dto.requests.AddSubcategoryRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateCategoryRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateSubcategoryRequestDto;
import com.quiroz.mypayments.dto.responses.CategoryResponseDto;
import com.quiroz.mypayments.dto.responses.SubcategoryResponseDto;
import com.quiroz.mypayments.entities.Category;
import com.quiroz.mypayments.exception.NotFoundException;
import com.quiroz.mypayments.mappers.CategoryMapper;
import com.quiroz.mypayments.repositories.CategoryRepository;
import com.quiroz.mypayments.services.impl.CategoryServiceImpl;
import jakarta.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
public class CategoryServiceTests {

    private static final Long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "TEST";
    private static final String CATEGORY_CODE = "TEST";
    private static final String CATEGORY_DESCRIPTION = "SOME DESCRIPTION";
    private static final Long PARENT_ID = 2L;

    private static final Long SUBCATEGORY_ID = 1L;
    private static final String SUBCATEGORY_NAME = "TEST";
    private static final String SUBCATEGORY_CODE = "TEST";
    private static final String SUBCATEGORY_DESCRIPTION = "SOME DESCRIPTION";


    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Test
    //@DisplayName("Given a category")
    void saveCategory_ThrowsEntityNotFoundException(){
        AddCategoryRequestDto requestDto = AddCategoryRequestDto.builder()
            .code(CATEGORY_NAME)
            .name(CATEGORY_CODE)
            .build();
        Category category = Category.builder().build();
        when(categoryRepository.findByNameAndCodeIgnoreCase(Mockito.any(),Mockito.any()))
            .thenReturn(Optional.of(category));

        assertThrows(EntityExistsException.class,
            ()-> categoryService.saveCategory(requestDto));
        verify(categoryRepository, times(1))
            .findByNameAndCodeIgnoreCase(Mockito.any(),Mockito.any());
    }

    @Test
    void saveCategory_Create() {
        AddCategoryRequestDto requestDto = AddCategoryRequestDto.builder()
            .code(CATEGORY_NAME)
            .name(CATEGORY_CODE)
            .build();
        when(categoryRepository.findByNameAndCodeIgnoreCase(Mockito.any(),Mockito.any()))
            .thenReturn(Optional.empty());
        when(categoryMapper.fromAddCategoryRequestDtoToCategory(requestDto))
            .thenReturn(mock(Category.class));
        when(categoryMapper.toCategoryResponseDto(Mockito.any())).thenReturn(mock(
            CategoryResponseDto.class));

        categoryService.saveCategory(requestDto);

        verify(categoryMapper, times(1))
            .fromAddCategoryRequestDtoToCategory(Mockito.any());

        verify(categoryMapper, times(1))
            .toCategoryResponseDto(Mockito.any());
    }

    @Test
    void updateCategory_NotFoundException() {
        UpdateCategoryRequestDto categoryRequestDto = UpdateCategoryRequestDto
            .builder()
            .id(CATEGORY_ID)
            .build();
        when(categoryRepository.findById(categoryRequestDto.getId()))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
            ()-> categoryService.updateCategory(categoryRequestDto));

        verify(categoryRepository, times(1))
            .findById(categoryRequestDto.getId());
    }

    @Test
    void updateCategory_Update() {
        final String CATEGORY_NAME_UPDATED = "NAME UPDATED";
        final String CATEGORY_CODE_UPDATED = "CODE UPDATED";
        final String CATEGORY_DESCRIPTION_UPDATED = "DESCRIPTION UPDATED";

        UpdateCategoryRequestDto categoryRequestDto = UpdateCategoryRequestDto
            .builder()
            .id(CATEGORY_ID)
            .code(CATEGORY_CODE_UPDATED)
            .name(CATEGORY_NAME_UPDATED)
            .description(CATEGORY_DESCRIPTION_UPDATED)
            .build();
        Category categoryFoundMock = Category.builder()
            .id(CATEGORY_ID)
            .code(CATEGORY_CODE)
            .name(CATEGORY_NAME)
            .description(CATEGORY_DESCRIPTION)
            .build();
        when(categoryRepository.findById(categoryRequestDto.getId()))
            .thenReturn(Optional.of(categoryFoundMock));

        CategoryResponseDto categoryResponseDto = CategoryResponseDto.builder()
            .id(CATEGORY_ID)
            .code(CATEGORY_CODE_UPDATED)
            .name(CATEGORY_NAME_UPDATED)
            .description(CATEGORY_DESCRIPTION_UPDATED)
            .build();
        when(categoryMapper.toCategoryResponseDto(categoryFoundMock))
            .thenReturn(categoryResponseDto);

        categoryService.updateCategory(categoryRequestDto);

        assertEquals(categoryResponseDto.getId(), CATEGORY_ID );
        assertEquals(categoryResponseDto.getCode(), CATEGORY_CODE_UPDATED );
        assertEquals(categoryResponseDto.getName(), CATEGORY_NAME_UPDATED );
        assertEquals(categoryResponseDto.getDescription(), CATEGORY_DESCRIPTION_UPDATED );
    }

    @Test
    void deleteCategory_NotFoundException() {
        when(categoryRepository.findById(CATEGORY_ID))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
            ()-> categoryService.deleteCategory(CATEGORY_ID));

        verify(categoryRepository, times(1))
            .findById(Mockito.any());
    }

    @Test
    void deleteCategory_Delete() {
        when(categoryRepository.findById(CATEGORY_ID))
            .thenReturn(Optional.of(mock(Category.class)));

        when(categoryRepository.findByParentId(Mockito.anyLong()))
            .thenReturn(mock(List.class));

        assertThrows(IllegalArgumentException.class,
            ()-> categoryService.deleteCategory(CATEGORY_ID));
    }

    @Test
    void getCategoryById_NotFoundException() {
        when(categoryRepository.findById(CATEGORY_ID))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
            () -> categoryService.getCategoryById(CATEGORY_ID));
    }

    @Test
    void getCategoryById_GetCategory() {
        Category categoryFoundMock = Category.builder()
            .id(CATEGORY_ID)
            .code(CATEGORY_CODE)
            .name(CATEGORY_NAME)
            .description(CATEGORY_DESCRIPTION)
            .build();
        when(categoryRepository.findById(CATEGORY_ID))
            .thenReturn(Optional.of(categoryFoundMock));

        when(categoryMapper.toCategoryResponseDto(Mockito.any()))
            .thenReturn(mock(CategoryResponseDto.class));

        categoryService.getCategoryById(CATEGORY_ID);

        verify(categoryMapper, times(1))
            .toCategoryResponseDto(Mockito.any());
    }

    @Test
    void saveSubcategory_Create() {
        Category parentCategoryMock = Category.builder()
            .id(PARENT_ID)
            .code(CATEGORY_CODE)
            .name(CATEGORY_NAME)
            .description(CATEGORY_DESCRIPTION)
            .build();

        when(categoryRepository.findById(Mockito.eq(PARENT_ID)))
            .thenReturn(Optional.of(parentCategoryMock));

        AddSubcategoryRequestDto subcategoryRequest = AddSubcategoryRequestDto.builder()
            .code(SUBCATEGORY_CODE)
            .name(SUBCATEGORY_NAME)
            .description(SUBCATEGORY_DESCRIPTION)
            .parentId(PARENT_ID)
            .build();
        when(categoryRepository.findByNameAndParentId(subcategoryRequest.getName(), PARENT_ID))
            .thenReturn(Optional.empty());

        Category subcategoryMock = Category.builder()
            .id(SUBCATEGORY_ID)
            .code(SUBCATEGORY_CODE)
            .name(SUBCATEGORY_NAME)
            .description(SUBCATEGORY_DESCRIPTION)
            .parentId(PARENT_ID)
            .build();
        when(categoryMapper.fromAddSubcategoryRequestDtoToCategory(subcategoryRequest))
            .thenReturn(subcategoryMock);

        when(categoryMapper.fromCategoryToSubcategoryResponseDto(subcategoryMock, parentCategoryMock))
            .thenReturn(mock(SubcategoryResponseDto.class));

        categoryService.saveSubcategory(PARENT_ID, subcategoryRequest);

        verify(categoryRepository, times(1))
            .save(Mockito.any());
    }

    @Test
    void saveSubcategory_ParentNotFoundException() {
        when(categoryRepository.findById(Mockito.eq(PARENT_ID)))
            .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
            ()-> categoryService.saveSubcategory(PARENT_ID, mock(AddSubcategoryRequestDto.class)));
    }

    @Test
    void saveSubcategory_EntityExistsException() {

        Category parentCategory = Category.builder().build();
        when(categoryRepository.findById(Mockito.eq(PARENT_ID)))
            .thenReturn(Optional.of(parentCategory));

        Category subcategory = Category.builder()
            .id(PARENT_ID)
            .name(SUBCATEGORY_NAME)
            .build();
        when(categoryRepository.findByNameAndParentId(SUBCATEGORY_NAME, PARENT_ID))
            .thenReturn(Optional.of(subcategory));

        AddSubcategoryRequestDto subcategoryRequest = AddSubcategoryRequestDto.builder()
            .name(SUBCATEGORY_NAME)
            .build();

        assertThrows(EntityExistsException.class,
            ()-> categoryService.saveSubcategory(PARENT_ID, subcategoryRequest));
    }

    @Test
    void updateSubcategory_NotFoundException() {
        when(categoryRepository.findById(Mockito.anyLong()))
            .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
            ()-> categoryService.updateSubcategory(mock(UpdateSubcategoryRequestDto.class)));
    }

    @Test
    void updateSubcategory_ParentNotFoundException() {
        when(categoryRepository.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(mock(Category.class)));

        when(categoryRepository.findById(Mockito.anyLong()))
            .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
            ()-> categoryService.updateSubcategory(mock(UpdateSubcategoryRequestDto.class)));
    }


    @Test
    void updateSubcategory_Update() {
        UpdateSubcategoryRequestDto requestDto = UpdateSubcategoryRequestDto.builder()
            .id(SUBCATEGORY_ID)
            .parentId(PARENT_ID)
            .build();
        Category subcategoryFound = Category.builder()
            .id(SUBCATEGORY_ID)
            .build();
        when(categoryRepository.findById(Mockito.eq(requestDto.getId())))
            .thenReturn(Optional.of(subcategoryFound));

        Category parentCategoryFound = Category.builder()
            .id(PARENT_ID)
            .build();
        when(categoryRepository.findById(Mockito.eq(requestDto.getParentId())))
            .thenReturn(Optional.of(parentCategoryFound));

        when(categoryMapper.fromCategoryToSubcategoryResponseDto(subcategoryFound, parentCategoryFound))
            .thenReturn(mock(SubcategoryResponseDto.class));

        categoryService.updateSubcategory(requestDto);

        verify(categoryRepository, times(1))
            .save(subcategoryFound);
    }

    @Test
    void deleteSubcategory_NotFoundException() {
        when(categoryRepository.findById(Mockito.anyLong()))
            .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
            ()-> categoryService.deleteSubcategory(Mockito.anyLong()));
    }

    @Test
    void deleteSubcategory_Delete() {
        Category categoryFound = Category.builder()
            .id(SUBCATEGORY_ID)
            .build();
        when(categoryRepository.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(categoryFound));

        categoryService.deleteSubcategory(Mockito.anyLong());

        verify(categoryRepository, times(1))
            .delete(categoryFound);
    }

    @Test
    void getSubcategoryById_NotFoundException() {
        when(categoryRepository.findById(Mockito.anyLong()))
            .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
            ()-> categoryService.getSubcategoryById(Mockito.anyLong()));

        verify(categoryRepository, times(1))
            .findById(Mockito.anyLong());
    }

    @Test
    void getSubcategoryById_ParentNotFoundException() {
        when(categoryRepository.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(mock(Category.class)));

        when(categoryRepository.findById(Mockito.anyLong()))
            .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
            ()-> categoryService.getSubcategoryById(Mockito.anyLong()));
    }

    @Test
    void getSubcategoryById_Get() {

        Category subcategoryFound = Category.builder()
            .id(SUBCATEGORY_ID)
            .parentId(PARENT_ID)
            .build();
        when(categoryRepository.findById(Mockito.eq(SUBCATEGORY_ID)))
            .thenReturn(Optional.of(subcategoryFound));

        Category parentCategoryFound = Category.builder()
            .id(PARENT_ID)
            .build();
        when(categoryRepository.findById(Mockito.eq(PARENT_ID)))
            .thenReturn(Optional.of(parentCategoryFound));

        when(categoryMapper.fromCategoryToSubcategoryResponseDto(subcategoryFound, parentCategoryFound))
            .thenReturn(mock(SubcategoryResponseDto.class));

        categoryService.getSubcategoryById(SUBCATEGORY_ID);

        verify(categoryRepository, times(1))
            .findById(Mockito.eq(PARENT_ID));

        verify(categoryRepository, times(1))
            .findById(Mockito.eq(SUBCATEGORY_ID));
    }

}
