package com.ntndev.ecommercespringboot.controller;


import com.ntndev.ecommercespringboot.components.LocalizationUtils;
import com.ntndev.ecommercespringboot.dtos.CategoryDTO;
import com.ntndev.ecommercespringboot.models.Category;
import com.ntndev.ecommercespringboot.responses.CategoryResponse;
import com.ntndev.ecommercespringboot.responses.UpdateCategoryResponse;
import com.ntndev.ecommercespringboot.services.CategoryService;
import com.ntndev.ecommercespringboot.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                           BindingResult result) {

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(CategoryResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_FAILED))
                    .errors(errorMessages)
                    .build());
        }
        Category category = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(CategoryResponse.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY))
                .category(category)
                .build());
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);

        return ResponseEntity.ok(UpdateCategoryResponse.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY));
    }
}
