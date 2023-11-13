package com.quiroz.mypayments.services.impl;

import com.quiroz.mypayments.dto.requests.AddCategoryRequestDto;
import com.quiroz.mypayments.dto.requests.AddSubcategoryRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateCategoryRequestDto;
import com.quiroz.mypayments.dto.responses.CategoryResponseDto;
import com.quiroz.mypayments.dto.responses.SubcategoryResponseDto;
import com.quiroz.mypayments.entities.Category;
import com.quiroz.mypayments.exception.NotFoundException;
import com.quiroz.mypayments.mappers.CategoryMapper;
import com.quiroz.mypayments.repositories.CategoryRepository;
import com.quiroz.mypayments.services.BaseService;
import com.quiroz.mypayments.services.CategoryService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl extends BaseService<Category, Long, CategoryRepository>
        implements CategoryService  {

    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto saveCategory(AddCategoryRequestDto addCategoryRequestDto) {
        log.info(String.format("Saving category with code: %s", addCategoryRequestDto.getCode()));
        var categoryFound = repositorio.findByNameAndCodeIgnoreCase(addCategoryRequestDto.getName(), addCategoryRequestDto.getCode());
        if (categoryFound.isPresent()) {
           throw new IllegalArgumentException(String.format("Already exists a category with name: %s and code: %s",
                   addCategoryRequestDto.getName(), addCategoryRequestDto.getCode()));
        }
        Category category = categoryMapper.toAddCategoryRequestDto(addCategoryRequestDto);
        repositorio.save(category);
        return categoryMapper.toCategoryResponseDto(category);
    }

    @Override
    public CategoryResponseDto updateCategory(UpdateCategoryRequestDto categoryRequestDto) {
        log.info(String.format("Updating category with id: %s", categoryRequestDto.getId()));
        Category category = repositorio.findById(categoryRequestDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id: %s not found.", categoryRequestDto.getId())));

        category.setCode(categoryRequestDto.getCode());
        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());

        this.edit(category);
        return categoryMapper.toCategoryResponseDto(category);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info(String.format("Deleting category with id: %s", id));
        Category category = repositorio.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id: %s not found.", id)));

        repositorio.delete(category);
    }

    @Override
    public List<CategoryResponseDto> getAllCategories(@NotNull int page,
                                                      @NotNull int size,
                                                      @NotNull String[] sort) {
        log.info("Getting categories");
        List<Sort.Order> orders = new ArrayList<>();
        if(sort[0].contains(",")) {
            for (String s : sort) {
                String[] sortArray = s.split(",");
                orders.add(new Sort.Order(getSortDirection(sortArray[1]), sortArray[0]));
            }
        } else {
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Category> categoryPage = repositorio.findAllByParentIdIsNullWithPagination(pageable);

        List<CategoryResponseDto> categoryDtos = new ArrayList<>();

        categoryPage.getContent().forEach(category ->
                categoryDtos.add(categoryMapper.toCategoryResponseDto(category)));

        return categoryDtos;
    }

    @Override
    public CategoryResponseDto getCategoryById(long id) {
        log.info(String.format("Getting category with id: %s", id));
        Category category = repositorio.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id: %s not found.", id)));
        return categoryMapper.toCategoryResponseDto(category);
    }


    @Override
    public List<SubcategoryResponseDto> saveSubcategories(long categoryId,
                                                          List<AddSubcategoryRequestDto> subcategoriesRequest) {

        log.info("Saving subcategory");
        var subcategories = subcategoriesRequest.stream()
                .map(subCategory -> Category.builder()
                        .parentId(categoryId)
                        .code(subCategory.getCode())
                        .name(subCategory.getName())
                        .description(subCategory.getDescription())
                        .createdAt(LocalDateTime.now())
                        .build())
                .toList();
        var categoriesSaved = this.saveAll(subcategories);
        return categoriesSaved.stream()
                .map(category -> SubcategoryResponseDto.builder()
                        .id(category.getId())
                        .code(category.getCode())
                        .name(category.getName())
                        .description(category.getDescription())
                        .build())
                .toList();
    }
    private Sort.Direction getSortDirection(String direction) {
        if(direction.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        } else if(direction.equalsIgnoreCase("desc")){
            return Sort.Direction.DESC;
        }
        return Sort.Direction.DESC;
    }

}
