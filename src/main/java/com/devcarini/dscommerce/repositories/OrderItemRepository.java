package com.devcarini.dscommerce.repositories;

import com.devcarini.dscommerce.entities.OrderItem;
import com.devcarini.dscommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
