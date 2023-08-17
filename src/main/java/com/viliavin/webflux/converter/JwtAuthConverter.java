package com.viliavin.webflux.converter;

import com.viliavin.webflux.service.JwtService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


public class JwtAuthConverter implements ServerAuthenticationConverter {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtService jwtService;


    public JwtAuthConverter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange)
            .mapNotNull(request -> request.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION))
            .map(auth -> auth.replace("Bearer ", ""))
            .filter(jwtService::validateAccessToken)
            .map(jwtService::parseAccessClaims)
            .map(this::getAuthenticationToken).log();
    }

    private Authentication getAuthenticationToken(Claims claims) {
        return new UsernamePasswordAuthenticationToken(
            claims.getSubject(),
            claims.getIssuer(),
            List.of(new SimpleGrantedAuthority(claims.get("role").toString()))
        );
    }

}
