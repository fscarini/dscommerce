package com.devcarini.dscommerce.dtos;

import com.devcarini.dscommerce.entities.User;

public record ClientDTO(
        Long id,
        String name
) {

    public ClientDTO(User entity){
        this(
                entity.getId(),
                entity.getName()
        );
    }

}
