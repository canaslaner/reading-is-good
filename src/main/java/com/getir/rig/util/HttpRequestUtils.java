package com.getir.rig.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@UtilityClass
public class HttpRequestUtils {

    public static Optional<String> parseClientIp(final HttpServletRequest request) {
        return Optional.ofNullable(request)
                .map(req -> req.getHeader(Constants.Security.X_FORWARDED_FOR_HEADER_NAME))
                .or(() ->
                        Optional.ofNullable(request)
                                .map(HttpServletRequest::getRemoteAddr)
                                .map(address -> Constants.Security.LOCALHOST_IP_V6.equals(address) ? Constants.Security.LOCALHOST_IP : address)
                );
    }

    public Optional<HttpServletRequest> getHttpServletRequest() {
        try {
            return Optional.of(RequestContextHolder.currentRequestAttributes())
                    .filter(ServletRequestAttributes.class::isInstance)
                    .map(ServletRequestAttributes.class::cast)
                    .map(ServletRequestAttributes::getRequest);
        } catch (final Exception e) {
            return Optional.empty();
        }
    }

    public Optional<String> getClientIpByContext() {
        return getHttpServletRequest().flatMap(HttpRequestUtils::parseClientIp);
    }
}
