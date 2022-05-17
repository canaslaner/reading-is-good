package com.getir.rig.config;

import com.getir.rig.security.JwtConfigurer;
import com.getir.rig.security.JwtProvider;
import com.getir.rig.security.model.enums.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@EnableWebSecurity
@Import(SecurityProblemSupport.class)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final JwtProvider jwtProvider;
    private final SecurityProblemSupport securityProblemSupport;

    private static final String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/h2-console/**",
            "/error"
    };

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers(AUTH_WHITELIST);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    @SuppressWarnings("java:S1192") // disabled this, because want to be sure paths are written down in full form
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .cors()
            .and() //Cross-origin resource sharing
                .csrf().disable()//Cross-site request forgery
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(securityProblemSupport)
                .accessDeniedHandler(securityProblemSupport)
            .and()
                .headers()
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            .and()
                .frameOptions()
                .deny()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                /* WHITELIST */
                .antMatchers(AUTH_WHITELIST).permitAll()
                /* com.getir.rig.controller.BackofficeController */
                .antMatchers(HttpMethod.POST, "/v1/backoffice/register").permitAll()
                /* com.getir.rig.controller.BookController */
                .antMatchers(HttpMethod.POST, "/v1/book").hasAuthority(Authority.BO_USER.name())
                .antMatchers(HttpMethod.GET, "/v1/book/*").hasAnyAuthority(Authority.CUSTOMER.name(), Authority.BO_USER.name())
                .antMatchers(HttpMethod.GET, "/v1/book").hasAnyAuthority(Authority.CUSTOMER.name(), Authority.BO_USER.name())
                .antMatchers(HttpMethod.POST, "/v1/book/*/stock").hasAnyAuthority(Authority.BO_USER.name())
                /* com.getir.rig.controller.CustomerController */
                .antMatchers(HttpMethod.POST, "/v1/customer/register").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/customer").hasAnyAuthority(Authority.CUSTOMER.name(), Authority.BO_USER.name())
                /* com.getir.rig.controller.LoginController */
                .antMatchers("/v1/login/authenticate").permitAll()
                /* com.getir.rig.controller.OrderController */
                .antMatchers(HttpMethod.POST, "/v1/order").hasAnyAuthority(Authority.CUSTOMER.name(), Authority.BO_USER.name())
                .antMatchers(HttpMethod.GET, "/v1/order/*").hasAnyAuthority(Authority.CUSTOMER.name(), Authority.BO_USER.name())
                .antMatchers(HttpMethod.GET, "/v1/order").hasAnyAuthority(Authority.CUSTOMER.name(), Authority.BO_USER.name())
                /* com.getir.rig.controller.StatisticsController */
                .antMatchers(HttpMethod.GET, "/v1/stats/*").hasAnyAuthority(Authority.BO_USER.name())
                /* DENY ALL OTHERS*/
                .antMatchers("/**").denyAll()
            .and()
                .httpBasic()
            .and()
                .apply(new JwtConfigurer(jwtProvider));
        // @formatter:on
    }
}
