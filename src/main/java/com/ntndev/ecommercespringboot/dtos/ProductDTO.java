package com.ntndev.ecommercespringboot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    private String name;
    @Min(value = 0, message = "Price must be greater than 0")
    @Max(value = 10000000, message = "Price must be less than 10000000")
    private float price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private String categoryId;
    private List<MultipartFile> files;

}
