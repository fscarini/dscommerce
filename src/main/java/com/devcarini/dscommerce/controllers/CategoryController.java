package com.devcarini.dscommerce.controllers;

import com.devcarini.dscommerce.dtos.CategoryDTO;
import com.devcarini.dscommerce.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @GetMapping
    public ResponseEntity<Set<CategoryDTO>> findAll(){
        Set<CategoryDTO> result = categoryService.findAll();
        return ResponseEntity.ok(result);
    }
}
