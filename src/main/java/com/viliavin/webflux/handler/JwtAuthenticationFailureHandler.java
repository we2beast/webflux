package com.viliavin.webflux.handler;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import reactor.core.publisher.Mono;

public class JwtAuthenticationFailureHandler extends ServerAuthenticationEntryPointFailureHandler {

    private final Counter failedAuthCounter = Metrics.counter("auth.failed");

    public JwtAuthenticationFailureHandler() {
        super(new HttpBasicServerAuthenticationEntryPoint());
    }

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        failedAuthCounter.increment();
        return super.onAuthenticationFailure(webFilterExchange, exception);
    }

}
