package com.viliavin.webflux.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viliavin.webflux.service.JwtService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

public class JwtAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final JwtService jwtService;
    private final Counter successAuthCounter = Metrics.counter("auth.success");

    public JwtAuthenticationSuccessHandler(JwtService jwtService, MeterRegistry meterRegistry) {
        this.jwtService = jwtService;
//        this.successAuthCounter = Metrics.counter("auth.success");
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        successAuthCounter.increment();
        logger.debug("auth: " + authentication);

        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();

        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        try {
            var tokens = objectMapper.writeValueAsBytes(jwtService.generateTokens(
                new User(authentication.getName(),
                    "empty",
                    authentication.getAuthorities())
            ));

            DataBuffer dataBuffer = response.bufferFactory().wrap(tokens);

            return response.writeWith(Mono.just(dataBuffer));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
