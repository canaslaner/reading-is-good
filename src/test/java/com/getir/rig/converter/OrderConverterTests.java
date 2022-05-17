package com.getir.rig.converter;

import com.getir.rig.model.Book;
import com.getir.rig.model.Order;
import com.getir.rig.model.enums.OrderStatus;
import com.getir.rig.security.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

class OrderConverterTests {

    @Test
    void testToDto_whenInputNull_thenShouldReturnNull() {
        Assertions.assertNull(OrderConverter.toDto(null));
    }

    @Test
    void testToDto_whenHappyPath_thenShouldCreateSuccessfully() {
        // given
        final var user = User.builder().id(2L).firstName("name").lastName("lastName").build();
        final var book = Book.builder().id(5L).build();
        final var order = Order.builder()
                .id(1L)
                .user(user)
                .book(book)
                .unitPrice(BigDecimal.TEN)
                .quantity(32)
                .totalPrice(BigDecimal.valueOf(320))
                .orderStatus(OrderStatus.NEW)
                .createdDate(Instant.now())
                .build();

        // when
        final var dto = OrderConverter.toDto(order);

        // then
        Assertions.assertEquals(order.getId(), dto.getId());
        Assertions.assertEquals(order.getUser().getFirstName(), dto.getFirstName());
        Assertions.assertEquals(order.getUser().getLastName(), dto.getLastName());
        Assertions.assertEquals(order.getBook().getId(), dto.getBook().getId());
        Assertions.assertEquals(order.getUser().getId(), dto.getUserId());
        Assertions.assertEquals(order.getUnitPrice(), dto.getUnitPrice());
        Assertions.assertEquals(order.getQuantity(), dto.getQuantity());
        Assertions.assertEquals(order.getTotalPrice(), dto.getTotalPrice());
        Assertions.assertEquals(order.getOrderStatus(), dto.getOrderStatus());
        Assertions.assertEquals(order.getCreatedDate(), dto.getOrderDate());
    }
}
