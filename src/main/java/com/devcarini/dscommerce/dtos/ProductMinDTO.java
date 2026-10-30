package com.devcarini.dscommerce.dtos;

import com.devcarini.dscommerce.entities.Product;

public record ProductMinDTO(
        Long id,
        String name,
        Double price,
        String imgUrl
) {

    public ProductMinDTO(Product entity){
        this(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getImgUrl()
        );
    }

}
