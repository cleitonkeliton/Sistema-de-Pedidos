package com.order.system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemRequestDTO {
    @NotNull(message = "O ID do produto é obrigatório")
    private Long productId;

    @Positive(message = "A quantidade deve ser maior que zero")
    private Integer quantity;
}