package com.ntndev.ecommercespringboot.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ntndev.ecommercespringboot.models.Product;
import com.ntndev.ecommercespringboot.models.ProductImage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductResponse extends BaseResponse{
    private Long id;
    private String name;
    private float price;
    private String thumbnail;
    private String description;

    @JsonProperty("product_images")
    private List<ProductImage> productImages = new ArrayList<>();

    @JsonProperty("category_id")
    private Long categoryId;

    public static ProductResponse fromProduct(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .productImages(product.getProductImages())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());

        return productResponse;
    }
}
