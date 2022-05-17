package com.getir.rig.controller.dto.order;

import com.getir.rig.controller.dto.OrderDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class GetOrderResponse {
    private OrderDto order;
}
