package com.viliavin.webflux.domain.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(value = "user")
public record UserEntity(
    @MongoId
    String id,
    String username,
    String password,
    Role role
) {
}
