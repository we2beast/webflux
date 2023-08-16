package com.viliavin.webflux.domain.model;

public record AuthTokens(
    String accessToken,
    String refreshToken
) {}
