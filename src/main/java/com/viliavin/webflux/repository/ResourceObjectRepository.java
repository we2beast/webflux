package com.viliavin.webflux.repository;

import com.viliavin.webflux.domain.entity.ResourceObjectEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ResourceObjectRepository extends ReactiveMongoRepository<ResourceObjectEntity, Integer> {

    Flux<ResourceObjectEntity> findByValueLikeIgnoreCase(String value);
    Flux<ResourceObjectEntity> findByPath(String path);

}
