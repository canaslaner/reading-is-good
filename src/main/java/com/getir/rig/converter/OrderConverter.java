package com.getir.rig.converter;

import com.getir.rig.controller.dto.OrderDto;
import com.getir.rig.model.Order;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderConverter {

    public static OrderDto toDto(final Order order) {
        if (order == null) {
            return null;
        }

        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .firstName(order.getUser().getFirstName())
                .lastName(order.getUser().getLastName())
                .book(BookConverter.toDto(order.getBook()))
                .quantity(order.getQuantity())
                .unitPrice(order.getUnitPrice())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getCreatedDate())
                .build();
    }
}