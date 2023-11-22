package com.quiroz.mypayments.services.impl;

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
import com.quiroz.mypayments.services.CategoryService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService  {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto saveCategory(AddCategoryRequestDto addCategoryRequestDto) {
        log.info(String.format("Saving category with code: %s", addCategoryRequestDto.getCode()));
        var categoryFound = categoryRepository.findByNameAndCodeIgnoreCase(addCategoryRequestDto.getName(), addCategoryRequestDto.getCode());
        if (categoryFound.isPresent()) {
           throw new EntityExistsException(String.format("Already exists a category with name: %s and code: %s",
                   addCategoryRequestDto.getName(), addCategoryRequestDto.getCode()));
        }
        Category category = categoryMapper.fromAddCategoryRequestDtoToCategory(addCategoryRequestDto);
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponseDto(category);
    }

    @Override
    public CategoryResponseDto updateCategory(UpdateCategoryRequestDto categoryRequestDto) {
        log.info(String.format("Updating category with id: %s", categoryRequestDto.getId()));
        Category category = categoryRepository.findById(categoryRequestDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id: %s not found.", categoryRequestDto.getId())));

        category.setCode(categoryRequestDto.getCode());
        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());

        categoryRepository.save(category);
        return categoryMapper.toCategoryResponseDto(category);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info(String.format("Deleting category with id: %s", id));
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id: %s not found.", id)));

        var subcategories = categoryRepository.findByParentId(category.getParentId());
        if (!subcategories.isEmpty()) {
            throw new IllegalArgumentException(String.format("Cannot delete because category: %s, because it has subcategories", id));
        }

        categoryRepository.delete(category);
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
        Page<Category> categoryPage = categoryRepository.findAllByParentIdIsNullWithPagination(pageable);

        List<CategoryResponseDto> categoryDtos = new ArrayList<>();

        categoryPage.getContent().forEach(category ->
                categoryDtos.add(categoryMapper.toCategoryResponseDto(category)));

        return categoryDtos;
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        log.info(String.format("Getting category with id: %s", id));
        Category category = categoryRepository.findById(id) //TODO use EntityNotFoundException
                .orElseThrow(() -> new NotFoundException(String.format("Category with id: %s not found.", id)));
        return categoryMapper.toCategoryResponseDto(category);
    }

    @Override
    public List<SubcategoryResponseDto> saveSubcategories(Long categoryId,
                                                          List<AddSubcategoryRequestDto> subcategoriesRequest) {

        log.info("Saving subcategory");
        var subcategories = subcategoriesRequest.stream()
                .map(subCategory -> Category.builder()
                        .parentId(categoryId)
                        .code(subCategory.getCode())
                        .name(subCategory.getName())
                        .description(subCategory.getDescription())
                        .build())
                .toList();

        var categoriesSaved = categoryRepository.saveAll(subcategories);
        return categoriesSaved.stream()
                .map(category -> SubcategoryResponseDto.builder()
                        .id(category.getId())
                        .code(category.getCode())
                        .name(category.getName())
                        .description(category.getDescription())
                        .build())
                .toList();
    }

    @Override
    public SubcategoryResponseDto saveSubcategory(Long categoryId,
                                                  AddSubcategoryRequestDto subcategoryRequest) {log.info("Saving subcategory: {} for the category: {}", subcategoryRequest.getName(), categoryId);

        var parentCategoryFound = categoryRepository.findById(categoryId)
            .orElseThrow(()-> new NotFoundException(String.format("CategoryId: %s not found.", categoryId)));

        Optional<Category> subcategoryFound = categoryRepository.findByNameAndParentId(subcategoryRequest.getName(), categoryId);
        if (subcategoryFound.isPresent()) {
              throw new EntityExistsException(String.format("Subcategory with name: %s and parentId: %s already exists", subcategoryRequest.getName(), categoryId));
        }

        Category subCategory = categoryMapper.fromAddSubcategoryRequestDtoToCategory(subcategoryRequest);
        categoryRepository.save(subCategory);

        return categoryMapper.fromCategoryToSubcategoryResponseDto(subCategory, parentCategoryFound);
    }

    @Override
    public SubcategoryResponseDto updateSubcategory(UpdateSubcategoryRequestDto requestDto) {

        log.info("Updating subcategoryId: {}", requestDto.getId());
        Category subcategoryFound = categoryRepository.findById(requestDto.getId())
            .orElseThrow(() -> new NotFoundException(String.format("SubcategoryId: %s not found.", requestDto)));

        var parentCategoryFound = categoryRepository.findById(requestDto.getParentId())
            .orElseThrow(()-> new NotFoundException(String.format("CategoryId: %s not found.", requestDto.getParentId())));

        subcategoryFound.setCode(requestDto.getCode());
        subcategoryFound.setName(requestDto.getName());
        subcategoryFound.setDescription(requestDto.getDescription());
        subcategoryFound.setParentId(parentCategoryFound.getId());
        categoryRepository.save(subcategoryFound);

        return categoryMapper.fromCategoryToSubcategoryResponseDto(subcategoryFound, parentCategoryFound);
    }

    @Override
    public void deleteSubcategory(Long id) {
        log.info("Deleting subcategory with id: {}", id);
        Category subcategoryFound = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("SubcategoryId: %s not found.", id)));

        categoryRepository.delete(subcategoryFound);
    }

    @Override
    public SubcategoryResponseDto getSubcategoryById(Long id) {
        log.info("Getting subcategory with id: {}", id);
        Category subcategoryFound = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("SubcategoryId: %s not found.", id)));

        var parentCategoryFound = categoryRepository.findById(subcategoryFound.getParentId())
            .orElseThrow(()-> new NotFoundException(String.format("CategoryId: %s not found.", subcategoryFound.getParentId())));

        return categoryMapper.fromCategoryToSubcategoryResponseDto(subcategoryFound, parentCategoryFound);
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
