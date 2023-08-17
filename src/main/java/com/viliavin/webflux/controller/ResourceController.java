package com.viliavin.webflux.controller;

import com.viliavin.webflux.domain.model.ResourceObject;
import com.viliavin.webflux.service.ResourceObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resource")
public class ResourceController {

    private final ResourceObjectService service;

    @PostMapping
    public Mono<Integer> createResourceObject(@RequestBody ResourceObject object) {
        return service.save(object);
    }

    @GetMapping("/{id}")
    public Mono<ResourceObject> getResourceObject(@PathVariable Integer id) {
        return service.get(id);
    }

}
