package com.ntndev.ecommercespringboot.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {
    @NotEmpty(message = "Category name is required")
    private String name;
}
