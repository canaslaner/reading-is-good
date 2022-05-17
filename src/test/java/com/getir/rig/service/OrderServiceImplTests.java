package com.getir.rig.service;

import com.getir.rig.exception.OrderInvalidBookException;
import com.getir.rig.exception.RigGenericException;
import com.getir.rig.model.Book;
import com.getir.rig.model.Order;
import com.getir.rig.repository.OrderRepository;
import com.getir.rig.security.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTests {

    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private BookService bookService;
    @Mock
    private UserService userService;

    @Test
    void testCreateOrder_whenContextMailIsNull_thenShouldThrowException() {
        // given
        final long bookId = 1L;
        final int quantity = 2;

        // when
        final var exception = assertThrows(RigGenericException.class,
                () -> orderService.createOrder(bookId, quantity));

        // then
        assertEquals("exception.userNotFound", exception.getMessage());
    }

    @Test
    void testCreateOrder_whenUserNotFound_thenShouldThrowException() {
        // given
        final long bookId = 1L;
        final int quantity = 2;
        final String email = "a@b.com";

        final var authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(email).when(authentication).getName();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.doReturn(null).when(userService).findActiveByEmail(email);

        // when
        final var exception = assertThrows(RigGenericException.class,
                () -> orderService.createOrder(bookId, quantity));

        // then
        assertEquals("exception.userNotFound", exception.getMessage());
    }

    @Test
    void testCreateOrder_whenBookNotFound_thenShouldThrowException() {
        // given
        final long bookId = 1L;
        final int quantity = 2;
        final String email = "a@b.com";
        final User mockUser = User.builder().email(email).build();

        final var authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(email).when(authentication).getName();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.doReturn(mockUser).when(userService).findActiveByEmail(email);
        Mockito.doReturn(Optional.empty()).when(bookService).findById(bookId);

        // when
        final var exception = assertThrows(OrderInvalidBookException.class,
                () -> orderService.createOrder(bookId, quantity));

        // then
        assertEquals("exception.order.bookNotFound", exception.getMessage());
    }

    @Test
    void testCreateOrder_whenBookIsNotSaleable_thenShouldThrowException() {
        // given
        final long bookId = 1L;
        final int quantity = 2;
        final String email = "a@b.com";
        final User mockUser = User.builder().email(email).build();
        final Book mockBook = Book.builder().id(bookId).price(BigDecimal.TEN).stock(0).active(false).build();

        final var authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(email).when(authentication).getName();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.doReturn(mockUser).when(userService).findActiveByEmail(email);
        Mockito.doReturn(Optional.of(mockBook)).when(bookService).findById(bookId);

        // when
        final var exception = assertThrows(OrderInvalidBookException.class,
                () -> orderService.createOrder(bookId, quantity));

        // then
        assertEquals("exception.order.notActiveBook", exception.getMessage());
    }

    @Test
    void testCreateOrder_whenBookOutOfStock_thenShouldThrowException() {
        // given
        final long bookId = 1L;
        final int quantity = 2;
        final String email = "a@b.com";
        final User mockUser = User.builder().email(email).build();
        final Book mockBook = Book.builder().id(bookId).price(BigDecimal.TEN).stock(0).active(true).build();

        final var authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(email).when(authentication).getName();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.doReturn(mockUser).when(userService).findActiveByEmail(email);
        Mockito.doReturn(Optional.of(mockBook)).when(bookService).findById(bookId);

        // when
        final var exception = assertThrows(OrderInvalidBookException.class,
                () -> orderService.createOrder(bookId, quantity));

        // then
        assertEquals("exception.order.outOfStockBook", exception.getMessage());
    }

    @Test
    void testCreateOrder_whenErrorOccurredWhileUpdatingStock_thenShouldThrowException() {
        // given
        final long bookId = 1L;
        final int quantity = 2;
        final String email = "a@b.com";
        final User mockUser = User.builder().email(email).build();
        final Book mockBook = Book.builder().id(bookId).stock(1).price(BigDecimal.TEN).active(true).build();

        final var authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(email).when(authentication).getName();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.doReturn(mockUser).when(userService).findActiveByEmail(email);
        Mockito.doReturn(Optional.of(mockBook)).when(bookService).findById(bookId);
        Mockito.doThrow(new RuntimeException("test_message")).when(bookService).save(mockBook);

        // when
        final var exception = assertThrows(RuntimeException.class,
                () -> orderService.createOrder(bookId, quantity));

        // then
        assertEquals("test_message", exception.getMessage());
    }

    @Test
    void testCreateOrder_whenErrorOccurredWhileSavingOrder_thenShouldThrowException() {
        // given
        final long bookId = 1L;
        final int quantity = 2;
        final String email = "a@b.com";
        final User mockUser = User.builder().email(email).build();
        final Book mockBook = Book.builder().id(bookId).stock(1).price(BigDecimal.TEN).active(true).build();

        final var authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(email).when(authentication).getName();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.doReturn(mockUser).when(userService).findActiveByEmail(email);
        Mockito.doReturn(Optional.of(mockBook)).when(bookService).findById(bookId);
        Mockito.doReturn(null).when(bookService).save(mockBook);
        Mockito.doThrow(new RuntimeException("test_message")).when(orderRepository).save(ArgumentMatchers.any());

        // when
        final var exception = assertThrows(RuntimeException.class,
                () -> orderService.createOrder(bookId, quantity));

        // then
        assertEquals("test_message", exception.getMessage());
    }

    @Test
    void testCreateOrder_whenHappyPath_thenShouldCreateOrder() {
        // given
        final long bookId = 1L;
        final int quantity = 2;
        final String email = "a@b.com";
        final User mockUser = User.builder().email(email).build();
        final Book mockBook = Book.builder().id(bookId).stock(1).price(BigDecimal.TEN).active(true).build();
        final Order mockOrder = Order.builder().build();

        final var authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(email).when(authentication).getName();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.doReturn(mockUser).when(userService).findActiveByEmail(email);
        Mockito.doReturn(Optional.of(mockBook)).when(bookService).findById(bookId);
        Mockito.doReturn(null).when(bookService).save(mockBook);
        Mockito.doReturn(mockOrder).when(orderRepository).save(ArgumentMatchers.any());

        // when
        final var resultOrder = orderService.createOrder(bookId, quantity);

        // then
        assertEquals(mockOrder, resultOrder);
    }

    @Test
    void testFindById_whenErrorOccurredInRepository_thenShouldThrowException() {
        // given
        final var id = 1L;
        Mockito.doThrow(new RuntimeException("test_message")).when(orderRepository).findById(id);

        // when
        final var exception = assertThrows(RuntimeException.class,
                () -> orderService.findById(id));

        // then
        assertEquals("test_message", exception.getMessage());
    }

    @Test
    void testFindById_whenHappyPath_thenSaveBookSuccessfully() {
        // given
        final var id = 1L;
        final var mockOrder = Mockito.mock(Order.class);
        Mockito.doReturn(Optional.of(mockOrder)).when(orderRepository).findById(id);

        // when
        final var savedOrder = orderService.findById(id);

        // then
        assertTrue(savedOrder.isPresent());
        assertEquals(mockOrder, savedOrder.get());
    }

    @Test
    void testQueryOrder_whenCurrentUserEmailIsNull_thenShouldThrowException() {
        // given
        final Pageable pageable = Mockito.mock(Pageable.class);

        // when
        final var exception = assertThrows(RigGenericException.class,
                () -> orderService.queryOrder(null, null, pageable));

        // then
        assertEquals("exception.userNotFound", exception.getMessage());
    }

    @Test
    void testQueryOrder_whenDatesAreNull_thenShouldQueryWithoutDateRange() {
        // given
        final Pageable pageable = Mockito.mock(Pageable.class);
        final String email = "a@b.com";

        final var authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(email).when(authentication).getName();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final var expectedMockPage = Mockito.mock(Page.class);
        Mockito.doReturn(expectedMockPage).when(orderRepository).findAllByUser_Email(email, pageable);

        // when
        final var pageResult = orderService.queryOrder(null, null, pageable);

        // then
        assertEquals(expectedMockPage, pageResult);
    }

    @Test
    void testQueryOrder_whenStartDateIsNull_thenShouldQueryWithoutDateRange() {
        // given
        final Instant endDate = Instant.now();
        final Pageable pageable = Mockito.mock(Pageable.class);
        final String email = "a@b.com";

        final var authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(email).when(authentication).getName();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final var expectedMockPage = Mockito.mock(Page.class);
        Mockito.doReturn(expectedMockPage).when(orderRepository).findAllByUser_Email(email, pageable);

        // when
        final var pageResult = orderService.queryOrder(null, endDate, pageable);

        // then
        assertEquals(expectedMockPage, pageResult);
    }

    @Test
    void testQueryOrder_whenEndDateIsNull_thenShouldQueryWithoutDateRange() {
        // given
        final Instant startDate = Instant.now();
        final Pageable pageable = Mockito.mock(Pageable.class);
        final String email = "a@b.com";

        final var authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(email).when(authentication).getName();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final var expectedMockPage = Mockito.mock(Page.class);
        Mockito.doReturn(expectedMockPage).when(orderRepository).findAllByUser_Email(email, pageable);

        // when
        final var pageResult = orderService.queryOrder(startDate, null, pageable);

        // then
        assertEquals(expectedMockPage, pageResult);
    }

    @Test
    void testQueryOrder_whenDateAreNoNull_thenShouldQueryWithDateRange() {
        // given
        final Instant startDate = Instant.now();
        final Instant endDate = Instant.now();
        final Pageable pageable = Mockito.mock(Pageable.class);
        final String email = "a@b.com";

        final var authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(email).when(authentication).getName();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final var expectedMockPage = Mockito.mock(Page.class);
        Mockito.doReturn(expectedMockPage).when(orderRepository)
                .findAllByUser_EmailAndCreatedDateBetween(email, startDate, endDate, pageable);

        // when
        final var pageResult = orderService.queryOrder(startDate, endDate, pageable);

        // then
        assertEquals(expectedMockPage, pageResult);
    }

    @Test
    void testGetStatistics_whenErrorOccurredInRepository_thenShouldThrowException() {
        // given
        final var id = 1L;
        Mockito.doThrow(new RuntimeException("test_message")).when(orderRepository).getMonthlyStats(id);

        // when
        final var exception = assertThrows(RuntimeException.class,
                () -> orderService.getStatistics(id));

        // then
        assertEquals("test_message", exception.getMessage());
    }

    @Test
    void testGetStatistics_whenHappyPath_thenSaveBookSuccessfully() {
        // given
        final var id = 1L;
        final var mockResult = Mockito.mock(List.class);
        Mockito.doReturn(mockResult).when(orderRepository).getMonthlyStats(id);

        // when
        final var returnedStatsResult = orderService.getStatistics(id);

        // then
        assertEquals(mockResult, returnedStatsResult);
    }
}
