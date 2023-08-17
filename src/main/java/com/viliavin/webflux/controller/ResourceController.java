package com.viliavin.webflux.controller;

import com.viliavin.webflux.domain.model.ResourceObject;
import com.viliavin.webflux.service.ResourceObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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

    @GetMapping("/search")
    public Flux<ResourceObject> searchByQuery(@RequestParam String q) {
        return service.searchByValue(q);
    }

    @GetMapping("/searchByPath")
    public Flux<ResourceObject> searchByPath(@RequestParam String path) {
        return service.searchByPath(path);
    }

}
