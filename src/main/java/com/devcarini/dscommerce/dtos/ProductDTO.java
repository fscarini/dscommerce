package com.devcarini.dscommerce.dtos;


import com.devcarini.dscommerce.entities.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.stream.Collectors;

public record ProductDTO(
        Long id,
        @NotBlank(message = "Campo requerido")
        @Size(min = 3, max = 80, message = "Nome precisa ter de 3 a 80 caracteres")
        String name,
        @NotBlank
        @Size(min = 10, message = "Descrição precisa ter no mínimo 10 caracteres")
        String description,
        @Positive(message = "O preço deve ser positivo")
        Double price,
        String imgUrl,
        @NotEmpty(message = "Cada produto requer no mínimo uma categoria")
        Set<CategoryDTO> categories
) {

    public ProductDTO(Product entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getImgUrl(),
                entity.getCategories().stream()
                        .map(CategoryDTO::new)
                        .collect(java.util.stream.Collectors.toSet())
        );
    }
}
