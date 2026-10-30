package com.devcarini.dscommerce.dtos;

import com.devcarini.dscommerce.entities.Payment;

import java.time.Instant;

public record PaymentDTO(
        Long id,
        Instant moment
) {

    public PaymentDTO(Payment entity){
        this(
                entity.getId(),
                entity.getMoment()
        );
    }

}
