package com.getir.rig.repository;

import com.getir.rig.model.Order;
import com.getir.rig.model.query.StatsResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByUser_Email(String email, Pageable pageable);

    Page<Order> findAllByUser_EmailAndCreatedDateBetween(String email, Instant startDate, Instant endDate, Pageable pageable);

    @Query("select Month(o.createdDate) as month, count(o) as totalOrderCount, sum(o.quantity) as totalBookCount, " +
            "sum(o.totalPrice) as totalPurchasedAmount from Order o where o.user.id=:id group by Month(o.createdDate)")
    List<StatsResult> getMonthlyStats(long id);
}
