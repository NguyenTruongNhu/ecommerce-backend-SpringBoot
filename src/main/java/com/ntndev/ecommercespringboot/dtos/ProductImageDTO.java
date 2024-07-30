package com.ntndev.ecommercespringboot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ntndev.ecommercespringboot.models.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductImageDTO {

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product id must be greater than 0")
    private Long productId;

    @JsonProperty("image_url")
    @Size(min = 5, max = 200, message = "Image url must be between 5 and 200 characters")
    private String imageUrl;
}
