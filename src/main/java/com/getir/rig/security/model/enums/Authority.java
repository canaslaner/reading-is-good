package com.getir.rig.security.model.enums;

import java.util.Arrays;
import java.util.Optional;

public enum Authority {
    CUSTOMER,
    BO_USER;

    public static Optional<Authority> of(final String name) {
        return Arrays.stream(Authority.values()).filter(authority -> authority.name().equals(name)).findFirst();
    }
}
