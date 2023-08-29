package com.viliavin.webflux.exception;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.ResponseEntity.badRequest;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionAdvice {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private MeterRegistry meterRegistry;

    @Autowired
    public ControllerExceptionAdvice(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity responseStatusException(ResponseStatusException ex) {
        logger.error("handling exception::" + ex);
        Counter counter = meterRegistry.counter(String.format("http.status.%d", ex.getStatusCode().value()));
        counter.increment();
        return badRequest().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity exception(Exception ex) {
        logger.error("handling exception::" + ex);
        Counter counter = meterRegistry.counter(String.format("http.exception.%s", ex.getClass().getName()));
        counter.increment();
        return badRequest().build();
    }

}
