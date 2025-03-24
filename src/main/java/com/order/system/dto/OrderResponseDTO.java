package com.order.system.dto.response;

import com.order.system.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private BigDecimal total;
    private CustomerResponseDTO customer;
    private List<OrderItemResponseDTO> items;
}