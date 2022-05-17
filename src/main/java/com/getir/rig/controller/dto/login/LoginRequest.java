package com.getir.rig.controller.dto.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class LoginRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
