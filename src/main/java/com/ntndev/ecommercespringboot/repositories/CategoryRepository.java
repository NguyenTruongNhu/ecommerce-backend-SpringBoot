package com.ntndev.ecommercespringboot.repositories;

import com.ntndev.ecommercespringboot.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
