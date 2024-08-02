package com.ntndev.ecommercespringboot.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ntndev.ecommercespringboot.models.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class RegisterResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("user")
    private User user;

}