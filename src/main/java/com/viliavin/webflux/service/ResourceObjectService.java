package com.viliavin.webflux.service;

import com.viliavin.webflux.domain.entity.ResourceObjectEntity;
import com.viliavin.webflux.domain.model.ResourceObject;
import com.viliavin.webflux.repository.ResourceObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ResourceObjectService {

    private final ResourceObjectRepository repository;

    public Mono<Integer> save(ResourceObject resourceObject) {
        return repository.save(new ResourceObjectEntity(
                resourceObject.getId(), resourceObject.getValue(),
                resourceObject.getPath())).map(ResourceObjectEntity::getId);

    }

    public Mono<ResourceObject> get(int id) {
        return repository.findById(id)
                .map(r -> new ResourceObject(r.getId(), r.getValue(), r.getPath()));
    }

}
