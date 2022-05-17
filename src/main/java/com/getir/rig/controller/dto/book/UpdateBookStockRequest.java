package com.getir.rig.controller.dto.book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UpdateBookStockRequest {
    @Min(0)
    private long stock;
}
