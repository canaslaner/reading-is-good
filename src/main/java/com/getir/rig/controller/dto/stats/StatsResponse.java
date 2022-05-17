package com.getir.rig.controller.dto.stats;

import com.getir.rig.model.query.StatsResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class StatsResponse {
    private List<StatsResult> stats;
}
