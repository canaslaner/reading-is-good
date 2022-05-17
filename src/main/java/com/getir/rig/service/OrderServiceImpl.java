package com.getir.rig.service;

import com.getir.rig.exception.OrderInvalidBookException;
import com.getir.rig.exception.RigGenericException;
import com.getir.rig.model.Book;
import com.getir.rig.model.Order;
import com.getir.rig.model.enums.OrderStatus;
import com.getir.rig.model.query.StatsResult;
import com.getir.rig.repository.OrderRepository;
import com.getir.rig.security.model.User;
import com.getir.rig.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final BookService bookService;
    private final UserService userService;

    @Override
    @Transactional
    public Order createOrder(final long bookId, final int quantity) {
        final var user = SecurityUtils.getCurrentUserEmail()
                .map(userService::findActiveByEmail)
                .orElseThrow(() -> new RigGenericException("exception.userNotFound"));

        final var book = findAndValidateBookToOrder(bookId);
        book.setStock(book.getStock() - 1);
        bookService.save(book);

        return orderRepository.save(createOrderEntity(user, book, quantity));
    }

    private Book findAndValidateBookToOrder(final Long bookId) {
        final var foundBook = bookService.findById(bookId)
                .orElseThrow(() -> new OrderInvalidBookException("exception.order.bookNotFound"));

        if (!foundBook.isActive()) {
            throw new OrderInvalidBookException("exception.order.notActiveBook");
        }

        if (foundBook.getStock() < 1) {
            throw new OrderInvalidBookException("exception.order.outOfStockBook");
        }

        return foundBook;
    }

    private Order createOrderEntity(final User user, final Book book, final int quantity) {
        return Order.builder().user(user)
                .book(book)
                .unitPrice(book.getPrice())
                .quantity(quantity)
                .totalPrice(book.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .orderStatus(OrderStatus.NEW).build();
    }

    @Override
    public Optional<Order> findById(final long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Page<Order> queryOrder(final Instant startDate, final Instant endDate, final Pageable pageable) {
        final var email = SecurityUtils.getCurrentUserEmail()
                .orElseThrow(() -> new RigGenericException("exception.userNotFound"));
        if (startDate == null || endDate == null) {
            return orderRepository.findAllByUser_Email(email, pageable);
        } else {
            return orderRepository.findAllByUser_EmailAndCreatedDateBetween(email, startDate, endDate, pageable);
        }
    }

    @Override
    public List<StatsResult> getStatistics(final long id) {
        return orderRepository.getMonthlyStats(id);
    }
}
