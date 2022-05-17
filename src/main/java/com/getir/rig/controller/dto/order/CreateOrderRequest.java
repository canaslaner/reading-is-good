package com.getir.rig.controller.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CreateOrderRequest {
    @NotNull
    private Long bookId;
    @Min(1)
    @Max(5)
    private int quantity;
}
