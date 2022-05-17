package com.getir.rig.listener;

import com.getir.rig.model.base.ModifiableEntity;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Slf4j
public class ModifiableEntityListener {
    @PostPersist
    private void afterSave(final ModifiableEntity entity) {
        log.info("[AUDIT-SAVE][{}] created with id:{}, by:{} ", entity.getClass().getSimpleName(), entity.getId(), entity.getCreatedBy());
    }

    @PostUpdate
    private void afterUpdate(final ModifiableEntity entity) {
        log.info("[AUDIT-UPDATE][{}] update with id:{}, by:{} ", entity.getClass().getSimpleName(), entity.getId(), entity.getModifiedBy());
    }
}
