package com.devcarini.dscommerce.services;

import com.devcarini.dscommerce.dtos.CategoryDTO;
import com.devcarini.dscommerce.entities.Category;
import com.devcarini.dscommerce.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Set<CategoryDTO> findAll(){
        Set<Category> result = categoryRepository.findAll().stream().collect(Collectors.toSet());
        return result.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toSet());
    }

}
