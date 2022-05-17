package com.getir.rig.controller.dto.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Instant createdDate;
}
