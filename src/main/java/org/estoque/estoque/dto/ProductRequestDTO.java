package org.estoque.estoque.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {
    @NotEmpty(message = "The product name can't be empty.")
    private String name;

    @NotNull(message = "The price may be defined.")
    @DecimalMin(value = "0.0", message = "The price can't be negative")
    private Double price;

    @NotNull(message = "The quantity may be defined.")
    @Min(value = 0, message = "The quantity can't be less than 0.")
    private Integer quantity;
}
