package com.order.system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDTO {
    @NotNull(message = "ID do cliente é obrigatório")
    private Long customerId;

    @NotEmpty(message = "Lista de itens não pode estar vazia")
    private List<OrderItemRequestDTO> items;
}