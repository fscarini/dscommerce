package com.devcarini.dscommerce.services;

import com.devcarini.dscommerce.dtos.OrderDTO;
import com.devcarini.dscommerce.dtos.ProductDTO;
import com.devcarini.dscommerce.entities.Order;
import com.devcarini.dscommerce.entities.OrderItem;
import com.devcarini.dscommerce.entities.Product;
import com.devcarini.dscommerce.enums.OrderStatus;
import com.devcarini.dscommerce.repositories.OrderItemRepository;
import com.devcarini.dscommerce.repositories.OrderRepository;
import com.devcarini.dscommerce.repositories.ProductRepository;
import com.devcarini.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso Não encontrado!"));

        authService.validateSelfOrAdmin(order.getClient().getId());

        return new OrderDTO(order);
    }

    @Transactional(readOnly = true)
    public OrderDTO insert(OrderDTO dto) {
        Order entity = new Order(
                Instant.now(),
                OrderStatus.WAITING_PAYMENT,
                userService.authenticated()
        );
        entity.getItems().addAll(
                dto.items().stream()
                        .map(x -> {
                            Product product = productRepository.getReferenceById(x.productId());
                            return new OrderItem(
                              entity,
                              product,
                              x.quantity(),
                              product.getPrice()
                            );
                        })
                        .collect(Collectors.toSet())
        );

        orderRepository.save(entity);
        orderItemRepository.saveAll(entity.getItems());

        return new OrderDTO(entity);
    }
}
