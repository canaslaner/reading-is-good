package com.getir.rig.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UtilityClass
public final class SecurityUtils {

    public static Optional<String> getCurrentUserEmail() {
        return getCurrentUserAuthentication().map(Principal::getName);
    }

    public static Optional<Authentication> getCurrentUserAuthentication() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication());
    }
}
