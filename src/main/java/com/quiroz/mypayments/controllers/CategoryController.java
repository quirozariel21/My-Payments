package com.quiroz.mypayments.controllers;

import com.quiroz.mypayments.dto.requests.AddCategoryRequestDto;
import com.quiroz.mypayments.dto.requests.UpdateCategoryRequestDto;
import com.quiroz.mypayments.dto.responses.CategoryResponseDto;
import com.quiroz.mypayments.services.CategoryService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping(path = "/api/v1/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the new category"),
            @ApiResponse(responseCode = "400", description = "Unable to create a category"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),

    })
    public ResponseEntity<CategoryResponseDto> saveCategory(@Valid @RequestBody AddCategoryRequestDto addCategoryRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.saveCategory(addCategoryRequestDto));
    }

    @PatchMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the category"),
            @ApiResponse(responseCode = "400", description = "Unable to update the category"),
            @ApiResponse(responseCode = "404", description = "Unable to find the category"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),

    })
    public ResponseEntity<CategoryResponseDto> updateCategory(@Valid @RequestBody UpdateCategoryRequestDto requestDto) {
        return ResponseEntity.ok(categoryService.updateCategory(requestDto));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the category"),
            @ApiResponse(responseCode = "404", description = "Unable to find the category"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),

    })
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully obtained the category"),
            @ApiResponse(responseCode = "400", description = "Unable to obtain the category"),
            @ApiResponse(responseCode = "404", description = "Unable to obtain the category"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),

    })
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully getting the categories"),
            @ApiResponse(responseCode = "204", description = "There are no categories"),
            @ApiResponse(responseCode = "400", description = "Unable to get categories"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),

    })
    public ResponseEntity<List<CategoryResponseDto>> getCategories(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam(defaultValue = "id, desc") String[] sort) {
       List<CategoryResponseDto> categories = categoryService.getAllCategories(page, size, sort);
       return categories.isEmpty()? ResponseEntity.noContent().build():
                                    ResponseEntity.ok(categories);
    }

}
