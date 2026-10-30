package com.devcarini.dscommerce.dtos;

import com.devcarini.dscommerce.entities.OrderItem;

public record OrderItemDTO(
        Long productId,
        String name,
        Double price,
        Integer quantity
) {

    public OrderItemDTO(OrderItem entity) {
        this(
                entity.getProduct().getId(),
                entity.getProduct().getName(),
                entity.getPrice(),
                entity.getQuantity()
        );
    }

    public Double getSubTotal(){
        return price * quantity;
    }

}
