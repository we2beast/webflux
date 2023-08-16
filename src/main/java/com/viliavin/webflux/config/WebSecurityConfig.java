package com.viliavin.webflux.config;

import com.viliavin.webflux.converter.LoginRequestConverter;
import com.viliavin.webflux.handler.JwtAuthenticationSuccessHandler;
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

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final ReactiveUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    @Bean
    public SecurityWebFilterChain jwtApiFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);

        var manager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        manager.setPasswordEncoder(bCryptPasswordEncoder);

        ServerAuthenticationConverter loginRequestConverter = new LoginRequestConverter();
        AuthenticationWebFilter authenticationFilter = new AuthenticationWebFilter(manager);
        authenticationFilter.setServerAuthenticationConverter(loginRequestConverter);
        authenticationFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler(jwtService));

        http
            .authorizeExchange((authorize) ->
                authorize.pathMatchers("/resource", "/resource/**").permitAll()
                    .anyExchange().authenticated())
            .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

}
