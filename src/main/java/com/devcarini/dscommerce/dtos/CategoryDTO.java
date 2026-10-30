package com.devcarini.dscommerce.dtos;

import com.devcarini.dscommerce.entities.Category;

public record CategoryDTO(
        Long id,
        String name
) {

    public CategoryDTO(Category entity){
        this(
                entity.getId(),
                entity.getName()
        );
    }

}
