package com.viliavin.webflux.service;

import com.viliavin.webflux.domain.entity.ResourceObjectEntity;
import com.viliavin.webflux.domain.model.ResourceObject;
import com.viliavin.webflux.repository.ResourceObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ResourceObjectService {

    private final ResourceObjectRepository repository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Integer> save(ResourceObject resourceObject) {
        return repository.save(new ResourceObjectEntity(
            resourceObject.getId(), resourceObject.getValue(),
            resourceObject.getPath())).map(ResourceObjectEntity::getId);
    }

    public Mono<ResourceObject> get(int id) {
        return repository.findById(id)
            .map(r -> new ResourceObject(r.getId(), r.getValue(), r.getPath()));
    }

    public Flux<ResourceObject> searchByValue(String q) {
        return repository.findByValueLikeIgnoreCase(String.format("%%%s%%", q))
            .map(r -> new ResourceObject(r.getId(), r.getValue(), r.getPath()));
    }

    public Flux<ResourceObject> searchByPath(String path) {
        return repository.findByPath(path)
            .map(r -> new ResourceObject(r.getId(), r.getValue(), r.getPath()));
    }
}
