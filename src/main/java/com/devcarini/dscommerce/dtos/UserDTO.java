package com.devcarini.dscommerce.dtos;

import com.devcarini.dscommerce.entities.Product;
import com.devcarini.dscommerce.entities.User;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record UserDTO(
        Long id,
        String name,
        String email,
        String phone,
        LocalDate birthDate,
        List<String> roles
) {

    public UserDTO(User entity){
        this(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getBirthDate(),
                transformGrantedAuthorityInRoles(entity)
        );
    }

    private static List<String> transformGrantedAuthorityInRoles (User entity){
        return entity.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

}
