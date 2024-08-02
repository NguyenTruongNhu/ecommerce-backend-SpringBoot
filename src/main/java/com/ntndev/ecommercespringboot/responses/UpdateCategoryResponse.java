package com.ntndev.ecommercespringboot.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class UpdateCategoryResponse {
    @JsonProperty("message")
    private String message;

}