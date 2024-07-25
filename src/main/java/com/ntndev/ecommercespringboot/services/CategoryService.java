package com.ntndev.ecommercespringboot.services;

import com.ntndev.ecommercespringboot.dtos.CategoryDTO;
import com.ntndev.ecommercespringboot.models.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category updateCategory(Long categoryId,CategoryDTO categoryDTO);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    void deleteCategory(Long id);

}
