package com.viliavin.webflux.converter;

import com.viliavin.webflux.domain.model.LoginRequest;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.springframework.core.ResolvableType.forClass;

public class LoginRequestConverter implements ServerAuthenticationConverter {

    private final Jackson2JsonDecoder decoder = new Jackson2JsonDecoder();

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return exchange.getRequest()
            .getBody()
            .map(Flux::just)
            .map(body -> {
                ResolvableType elementType = forClass(LoginRequest.class);
                return decoder.decodeToMono(body, elementType, MediaType.APPLICATION_JSON, Collections.emptyMap()).cast(LoginRequest.class);
            }).next()
            .flatMap(x -> x)
            .map(loginRequest ->
                UsernamePasswordAuthenticationToken.unauthenticated(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
    }

}
