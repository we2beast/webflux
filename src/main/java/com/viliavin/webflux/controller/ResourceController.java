package com.viliavin.webflux.controller;

import com.viliavin.webflux.domain.model.ResourceObject;
import com.viliavin.webflux.service.ResourceObjectService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resource")
public class ResourceController {

    private final ResourceObjectService service;

    @PostMapping
    public ResponseEntity<Mono<Integer>> createResourceObject(@RequestBody ResourceObject object) {
        val result = service.save(object);
        return ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<ResourceObject>> getResourceObject(@PathVariable Integer id) {
        return ok(service.get(id));
    }

}
