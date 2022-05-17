package com.getir.rig.model.base;

import com.getir.rig.listener.ModifiableEntityListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@EntityListeners({AuditingEntityListener.class, ModifiableEntityListener.class})
public abstract class ModifiableEntity {

    @CreatedBy
    @Column(nullable = false, length = 300, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(length = 300)
    private String modifiedBy;

    @LastModifiedDate
    @Column
    private Instant lastModifiedDate = Instant.now();

    public abstract Long getId();
}
