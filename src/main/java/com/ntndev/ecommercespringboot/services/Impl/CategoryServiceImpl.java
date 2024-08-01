package com.ntndev.ecommercespringboot.services.Impl;

import com.ntndev.ecommercespringboot.dtos.CategoryDTO;
import com.ntndev.ecommercespringboot.models.Category;
import com.ntndev.ecommercespringboot.repositories.CategoryRepository;
import com.ntndev.ecommercespringboot.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    // Tạo mới một Category từ CategoryDTO
    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    // Cập nhật một Category theo ID và dữ liệu từ CategoryDTO
    @Override
    public Category updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category existingCategory = getCategoryById(categoryId);
        existingCategory.setName(categoryDTO.getName());
        return categoryRepository.save(existingCategory);
    }

    // Lấy một Category theo ID
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    // Lấy tất cả các Category
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Xóa một Category theo ID
    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
