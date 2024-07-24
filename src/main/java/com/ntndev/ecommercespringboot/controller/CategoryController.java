package com.ntndev.ecommercespringboot.controller;


import com.ntndev.ecommercespringboot.dtos.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @GetMapping("")
    public ResponseEntity<String> getAllCategories(){
        return  ResponseEntity.ok("Hello");
    }
    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                 BindingResult result){

        if (result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        return  ResponseEntity.ok("this is create " + categoryDTO.getName());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id){
        return  ResponseEntity.ok("this is update id: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        return  ResponseEntity.ok("this is delete id: " + id);
    }
}
