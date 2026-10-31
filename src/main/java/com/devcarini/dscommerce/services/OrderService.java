package com.devcarini.dscommerce.services;

import com.devcarini.dscommerce.dtos.OrderDTO;
import com.devcarini.dscommerce.entities.Order;
import com.devcarini.dscommerce.repositories.OrderRepository;
import com.devcarini.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso Não encontrado!"));
        return new OrderDTO(order);
    }
}
