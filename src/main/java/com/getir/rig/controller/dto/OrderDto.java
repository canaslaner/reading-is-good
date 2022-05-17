package com.getir.rig.controller.dto;

import com.getir.rig.model.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class OrderDto {
    private long id;
    private long userId;
    private String firstName;
    private String lastName;
    private BookDto book;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;
    private Instant orderDate;
}