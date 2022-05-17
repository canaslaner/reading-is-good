package com.getir.rig.controller;

import com.getir.rig.controller.dto.OrderDto;
import com.getir.rig.controller.dto.order.CreateOrderRequest;
import com.getir.rig.controller.dto.order.CreateOrderResponse;
import com.getir.rig.controller.dto.order.GetOrderResponse;
import com.getir.rig.converter.OrderConverter;
import com.getir.rig.exception.RecordNotFoundException;
import com.getir.rig.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Slf4j
@RestController
@RequestMapping(value = "/v1/order",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Order API", description = "Manages order-related processes")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Returns customer orders",
            description = "Returns list of orders owned logged in customer",
            security = @SecurityRequirement(name = "Bearer"))
    public CreateOrderResponse createOrder(@Valid @RequestBody final CreateOrderRequest request) {
        final var order = orderService.createOrder(request.getBookId(), request.getQuantity());
        return CreateOrderResponse.builder().order(OrderConverter.toDto(order)).build();
    }

    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    @Operation(summary = "Returns customer orders",
            description = "Returns list of orders owned logged in customer",
            security = @SecurityRequirement(name = "Bearer"))
    public GetOrderResponse queryById(@PathVariable final long id) {
        final var order = orderService.findById(id)
                .map(OrderConverter::toDto)
                .orElseThrow(() -> new RecordNotFoundException("exception.orderNotFound"));
        return GetOrderResponse.builder().order(order).build();
    }

    @GetMapping(consumes = MediaType.ALL_VALUE)
    @Operation(summary = "Returns customer orders",
            description = "Returns list of orders owned logged in customer",
            security = @SecurityRequirement(name = "Bearer"))
    public Page<OrderDto> queryOrder(@RequestParam final Instant startDate, @RequestParam final Instant endDate,
                                     @ParameterObject @NotNull @Valid final Pageable pageable) {
        return orderService.queryOrder(startDate, endDate, pageable)
                .map(OrderConverter::toDto);
    }
}
