package com.getir.rig.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BookDto {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long id;
    @NotEmpty
    private String isbn;
    @NotEmpty
    private String sku;
    @NotEmpty
    private String name;
    @NotEmpty
    private String author;
    private String description;
    @Min(0)
    private Long stock;
    @DecimalMin("0.00")
    private BigDecimal price;
    @NotNull
    private Boolean active;
}

