package com.getir.rig.model;

import com.getir.rig.model.base.ModifiableEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Table(name = "books")
@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@NoArgsConstructor
public class Book extends ModifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookSequenceGenerator")
    @SequenceGenerator(name = "bookSequenceGenerator", initialValue = 10, allocationSize = 100)
    private Long id;

    @Column
    private String isbn;

    @Column
    private String sku;

    @Column
    private String name;

    @Column
    private String author;

    @Column
    private String description;

    @Min(0)
    @Column
    private long stock;

    @DecimalMin(value = "0.0")
    @Column
    private BigDecimal price;

    @Column
    private boolean active;

    @Version
    private long version;
}

