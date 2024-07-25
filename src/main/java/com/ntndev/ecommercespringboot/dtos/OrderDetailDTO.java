package com.ntndev.ecommercespringboot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetailDTO {

    @JsonProperty("order_id")
    @Min(value = 1, message = "Order ID must be greater than 0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product ID must be greater than 0")
    private Long productId;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Float price;


    @Min(value = 1, message = "Number of products must be greater than 0")
    @JsonProperty("number_of_products")
    private Integer numberOfProducts;

    @Min(value = 0, message = "Total money must be greater than or equal to 0")
    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;
}
