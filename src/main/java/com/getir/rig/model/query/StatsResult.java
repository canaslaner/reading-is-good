package com.getir.rig.model.query;

import java.math.BigDecimal;

public interface StatsResult {
    int getMonth();

    Long getTotalOrderCount();

    Long getTotalBookCount();

    BigDecimal getTotalPurchasedAmount();
}
