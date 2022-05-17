package com.getir.rig.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;

@Configuration
public class CorsConfig {
    CorsConfiguration corsConfiguration;

    @PostConstruct
    protected void postCons() {
        this.corsConfiguration = new CorsConfiguration();
        this.corsConfiguration.addAllowedOrigin(CorsConfiguration.ALL);
        this.corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        this.corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        this.corsConfiguration.addExposedHeader("Authorization,Link,X-Total-Count");
        this.corsConfiguration.setAllowCredentials(false);
        this.corsConfiguration.setMaxAge(1800L);
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", this.corsConfiguration);
        return new CorsFilter(source);
    }
}
