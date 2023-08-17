package com.viliavin.webflux.controller;

import com.viliavin.webflux.domain.model.AuthTokens;
import com.viliavin.webflux.domain.model.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    @GetMapping("login")
    public Mono<AuthTokens> login(@Valid @RequestBody LoginRequest credentials) {
        throw new IllegalStateException("Add Spring Security to handle authentication");
    }

}
