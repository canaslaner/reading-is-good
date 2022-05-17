package com.getir.rig.controller;

import com.getir.rig.controller.dto.stats.StatsResponse;
import com.getir.rig.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping(value = "/v1/stats", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Statistics API", description = "Manages customer statistics-related processes")
@RequiredArgsConstructor
public class StatisticsController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public StatsResponse get(@PathVariable @NotNull final Long id) {
        return StatsResponse.builder().stats(orderService.getStatistics(id)).build();
    }
}
