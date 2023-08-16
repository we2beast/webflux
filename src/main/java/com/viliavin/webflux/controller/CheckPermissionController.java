package com.viliavin.webflux.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CheckPermissionController {

    @GetMapping("user")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Mono<String>> user() {
        return ok(Mono.just("User permission"));
    }

    @GetMapping("admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Mono<String>> admin() {
        return ok(Mono.just("Admin permission"));
    }

}
