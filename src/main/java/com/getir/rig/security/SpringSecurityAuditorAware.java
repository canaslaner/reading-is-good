package com.getir.rig.security;

import com.getir.rig.util.Constants;
import com.getir.rig.util.HttpRequestUtils;
import com.getir.rig.util.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

// source of createdBy and lastModifiedBy
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        final String foundUser = SecurityUtils.getCurrentUserEmail().orElse(Constants.Security.ANONYMOUS_USER);
        if (foundUser.equals(Constants.Security.ANONYMOUS_USER)) {
            final Optional<String> ip = HttpRequestUtils.getClientIpByContext();
            if (ip.isPresent()) {
                return ip.map(value -> Constants.Security.ANONYMOUS_USER + "@" + value);
            }
        }
        return Optional.of(foundUser);
    }
}
