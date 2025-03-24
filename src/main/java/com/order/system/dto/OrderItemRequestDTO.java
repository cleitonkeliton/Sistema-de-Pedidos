package com.order.system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderItemRequestDTO {
    @NotNull(message = "Product ID é obrigatório")
    private Long productId;

    @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
    private Integer quantity;
}
