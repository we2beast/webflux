package com.viliavin.webflux.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ExceptionController {

    @GetMapping("exception")
    public Mono<String> exception() throws Exception {
        throw new Exception("test me");
    }

    @GetMapping("exception/status-code")
    public Mono<String> statusCodeException() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "test me");
    }

}
