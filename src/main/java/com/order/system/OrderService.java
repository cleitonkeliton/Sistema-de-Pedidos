package com.order.system.service;

import com.order.system.dto.request.OrderRequestDTO;
import com.order.system.dto.response.OrderResponseDTO;
import com.order.system.exception.ResourceNotFoundException;
import com.order.system.model.*;
import com.order.system.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> items = orderRequest.getItems().stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

                    // Valida estoque e atualiza
                    product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
                    productRepository.save(product);

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setOrder(order);
                    return orderItem;
                }).toList();

        order.setItems(items);
        order.calculateTotal(); // Método para calcular o total do pedido
        return mapToResponseDTO(orderRepository.save(order));
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        // Implemente a conversão para DTO
        return new OrderResponseDTO();
    }
}