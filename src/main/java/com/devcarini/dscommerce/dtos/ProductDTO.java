package com.devcarini.dscommerce.dtos;


import com.devcarini.dscommerce.entities.Product;

public record ProductDTO(
        Long id,
        String name,
        String description,
        Double price,
        String imgUrl
){

    public ProductDTO(Product entity){
        this(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getImgUrl()
        );
    }

}
