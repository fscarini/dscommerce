package com.devcarini.dscommerce.services;

import com.devcarini.dscommerce.dtos.ProductDTO;
import com.devcarini.dscommerce.entities.Product;
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

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso Não encontrado!"));
        return new ProductDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){
        Page<Product> result = productRepository.findAll(pageable);
        return result.map(x -> new ProductDTO(x));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product entitty = new Product(dto);
        entitty = productRepository.save(entitty);
        return new ProductDTO(entitty);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto){
        try{
            Product entitty = productRepository.getReferenceById(id);
            entitty.setName(dto.name());
            entitty.setDescription(dto.description());
            entitty.setPrice(dto.price());
            entitty.setImgUrl(dto.imgUrl());
            entitty = productRepository.save(entitty);
            return new ProductDTO(entitty);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso não encontrado!");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if(!productRepository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado!");
        }

        try{
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }

    }

}
