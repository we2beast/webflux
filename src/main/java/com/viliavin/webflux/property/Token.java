package com.viliavin.webflux.property;

public record Token(
    Long ttlInSeconds,
    String key
) {
}
