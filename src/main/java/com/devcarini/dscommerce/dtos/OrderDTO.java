package com.devcarini.dscommerce.dtos;

import com.devcarini.dscommerce.entities.Order;

import com.devcarini.dscommerce.enums.OrderStatus;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.List;

public record OrderDTO(
        Long id,
        Instant moment,
        OrderStatus status,
        ClientDTO client,
        PaymentDTO payment,
        @NotEmpty(message = "Deve ter pelo menos um produto")
        List<OrderItemDTO> items
) {

    public OrderDTO(Order entity){
        this(
                entity.getId(),
                entity.getMoment(),
                entity.getStatus(),
                new ClientDTO(entity.getClient()),
                entity.getPayment() != null ? new PaymentDTO(entity.getPayment()) : null,
                entity.getItems().stream().map(OrderItemDTO::new).toList()
        );
    }

    public Double getTotal(){
        return items.stream().mapToDouble(OrderItemDTO::getSubTotal).sum();
    }

}
