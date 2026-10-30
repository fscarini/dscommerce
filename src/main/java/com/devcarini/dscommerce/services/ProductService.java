package com.devcarini.dscommerce.services;

import com.devcarini.dscommerce.dtos.CategoryDTO;
import com.devcarini.dscommerce.dtos.ProductDTO;
import com.devcarini.dscommerce.dtos.ProductMinDTO;
import com.devcarini.dscommerce.entities.Category;
import com.devcarini.dscommerce.entities.Product;
import com.devcarini.dscommerce.repositories.CategoryRepository;
import com.devcarini.dscommerce.repositories.ProductRepository;
import com.devcarini.dscommerce.services.exceptions.DatabaseException;
import com.devcarini.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso Não encontrado!"));
        return new ProductDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll(String name, Pageable pageable) {
        Page<Product> result = productRepository.searchByName(name, pageable);
        return result.map(x -> new ProductMinDTO(x));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();

        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setImgUrl(dto.imgUrl());

        entity.getCategories().addAll(convertCategoryDTOToEntity(dto.categories()));

        entity = productRepository.save(entity);

        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado!"));

        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setImgUrl(dto.imgUrl());

        entity.getCategories().clear();
        entity.getCategories().addAll(convertCategoryDTOToEntity(dto.categories()));

        return new ProductDTO(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado!");
        }

        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }

    }

    private Set<Category> convertCategoryDTOToEntity(Set<CategoryDTO> categories) {
        Set<Long> ids = categories.stream()
                .map(CategoryDTO::id)
                .collect(Collectors.toSet());

        Set<Category> entities = new HashSet<>(categoryRepository.findAllById(ids));

        if (entities.size() != ids.size()) {
            throw new ResourceNotFoundException("Uma ou mais categorias não foram encontradas!");
        }

        return entities;
    }

}
