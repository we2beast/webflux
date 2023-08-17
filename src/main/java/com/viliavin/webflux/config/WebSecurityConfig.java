package com.viliavin.webflux.config;

import com.viliavin.webflux.converter.JwtAuthConverter;
import com.viliavin.webflux.converter.LoginRequestConverter;
import com.viliavin.webflux.handler.JwtAuthenticationSuccessHandler;
import com.viliavin.webflux.manager.BearerAuthorizationManager;
import com.viliavin.webflux.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    private final ReactiveUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    @Bean
    public SecurityWebFilterChain jwtApiFilterChain(ServerHttpSecurity http) {
        var manager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        manager.setPasswordEncoder(bCryptPasswordEncoder);

        ServerAuthenticationConverter loginRequestConverter = new LoginRequestConverter();
        AuthenticationWebFilter authenticationFilter = new AuthenticationWebFilter(manager);
        authenticationFilter.setServerAuthenticationConverter(loginRequestConverter);
        authenticationFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler(jwtService));
        authenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/login"));

        AuthenticationWebFilter bearerAuthWebFilter = new AuthenticationWebFilter(new BearerAuthorizationManager());
        bearerAuthWebFilter.setServerAuthenticationConverter(new JwtAuthConverter(jwtService));

        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .authorizeExchange((authorize) ->
                authorize
                    .pathMatchers("/webjars/swagger-ui/**", "/swagger-ui/**", "/swagger-ui.html",
                        "/v3/api-docs", "/v3/api-docs/**",
                        "/v3/api-docs.yaml", "/v3/api-docs.yaml/**").permitAll()
                    .pathMatchers("/resource", "/resource/**").permitAll()
                    .anyExchange().authenticated())
            .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .addFilterAt(bearerAuthWebFilter, SecurityWebFiltersOrder.AUTHORIZATION);

        return http.build();
    }

}
