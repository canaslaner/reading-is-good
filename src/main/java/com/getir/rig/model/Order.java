package com.getir.rig.model;

import com.getir.rig.model.base.ModifiableEntity;
import com.getir.rig.model.enums.OrderStatus;
import com.getir.rig.security.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Order extends ModifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSequenceGenerator")
    @SequenceGenerator(name = "orderSequenceGenerator", allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @Column
    private BigDecimal unitPrice;

    @Column
    private int quantity;

    @Column
    private BigDecimal totalPrice;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.NEW;
}

