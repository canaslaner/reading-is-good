package com.getir.rig.security;

import com.getir.rig.util.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider implements InitializingBean {

    @Value("${security.base64-secret}")
    private String base64Secret;
    @Value("${security.token.lifetime:60M}")
    private Duration tokenLifetime;
    @Value("${security.token.logEnabled:false}")
    private boolean loggingEnabled;

    private Key hashedSecretKey;
    private JwtParser jwtParser;

    @Override
    public void afterPropertiesSet() {
        final var keyBytes = Decoders.BASE64.decode(base64Secret);
        this.hashedSecretKey = Keys.hmacShaKeyFor(keyBytes);

        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(hashedSecretKey)
                .build();
    }

    public String createToken(final Authentication authentication) {
        final var joinedAuthorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(Constants.Security.JWT_CLAIM_AUTHORITY_DELIMITER));

        final var expiryDate = Date.from(Instant.now().plus(tokenLifetime));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(Constants.Security.JWT_CLAIM_AUTHORITIES_KEY, joinedAuthorities)
                .setExpiration(expiryDate)
                .signWith(hashedSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(final String jwtToken) {
        final var claims = jwtParser.parseClaimsJws(jwtToken).getBody();

        final var authorities = Arrays
                .stream(claims.get(Constants.Security.JWT_CLAIM_AUTHORITIES_KEY)
                        .toString().split(Constants.Security.JWT_CLAIM_AUTHORITY_DELIMITER))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        final var user = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(user, jwtToken, authorities);
    }

    public boolean validateToken(final String jwtToken) {
        try {
            //if parsing process is successfully done, then it is a valid token.
            jwtParser.parseClaimsJws(jwtToken);
            return true;
        } catch (final Exception e) {
            if (loggingEnabled) {
                logValidationExceptions(e);
            }
        }
        return false;
    }

    private void logValidationExceptions(final Exception e) {
        String message;
        if (e instanceof SecurityException || e instanceof MalformedJwtException) {
            message = "Invalid JWT signature.";
        } else if (e instanceof ExpiredJwtException) {
            message = "Expired JWT token.";
        } else if (e instanceof UnsupportedJwtException) {
            message = "Unsupported JWT token.";
        } else if (e instanceof IllegalArgumentException) {
            message = "JWT token compact of handler are invalid.";
        } else {
            message = "JWT token unknown validation error, " + e.getMessage();
        }

        if (log.isTraceEnabled()) {
            log.trace(message, e);
        } else {
            log.info(message);
        }
    }
}
