package com.viliavin.webflux.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CheckPermissionController {

    @GetMapping("user")
    @PreAuthorize("hasRole('USER')")
    public Mono<String> user() {
        return Mono.just("User permission");
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<String> admin() {
        return Mono.just("Admin permission");
    }

}
