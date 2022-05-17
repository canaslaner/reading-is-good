package com.getir.rig.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.getir.rig.model.base.ModifiableEntity;
import com.getir.rig.security.model.enums.Authority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SQLDelete(sql = "update User set active = false WHERE id = ?", check = ResultCheckStyle.COUNT)
public class User extends ModifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", initialValue = 10, allocationSize = 100)
    private Long id;

    @Email
    @Size(min = 5, max = 200)
    @Column(length = 200, nullable = false)
    private String email;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "hashed_pass", length = 60, nullable = false)
    private String password;

    @Size(max = 60)
    @Column(length = 60, nullable = false)
    private String firstName;

    @Size(max = 60)
    @Column(length = 60, nullable = false)
    private String lastName;

    @Column(nullable = false)
    private boolean active;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;
}

