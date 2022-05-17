package com.getir.rig.controller.dto.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class RegisterRequest {
    @Email
    @NotEmpty
    private String email;
    @Length(min = 5, max = 10)
    @NotEmpty
    private String password;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
}
