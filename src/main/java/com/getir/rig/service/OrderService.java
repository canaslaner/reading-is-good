package com.getir.rig.service;

import com.getir.rig.model.Order;
import com.getir.rig.model.query.StatsResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order createOrder(long bookId, int quantity);

    Optional<Order> findById(long id);

    Page<Order> queryOrder(Instant startDate, Instant endDate, Pageable pageable);

    List<StatsResult> getStatistics(long id);
}
