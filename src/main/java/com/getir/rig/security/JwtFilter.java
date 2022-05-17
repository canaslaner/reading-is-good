package com.getir.rig.security;

import com.getir.rig.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final String jwt = resolveToken(servletRequest);

        if (StringUtils.hasText(jwt) && this.jwtProvider.validateToken(jwt)) {
            final var authentication = this.jwtProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(final ServletRequest servletRequest) {
        //to resolve on http header
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final String bearerToken = request.getHeader(Constants.Security.AUTHORIZATION_HEADER_NAME);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.Security.BEARER_TOKEN_PREFIX)) {
            return bearerToken.substring(Constants.Security.BEARER_TOKEN_PREFIX_LENGTH);
        }

        return null;
    }
}
