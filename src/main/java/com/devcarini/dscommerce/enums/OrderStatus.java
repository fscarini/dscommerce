package com.devcarini.dscommerce.enums;

public enum OrderStatus {
    WAITING_PAYMENT (0),
    PAID (1),
    SHIPPED (2),
    DELIVERED (3),
    CANCELED (4);

    private final int number;

    OrderStatus(int number){
        this.number = number;
    }

    public int getOrderStatusNumber(){
        return this.number;
    }
}
