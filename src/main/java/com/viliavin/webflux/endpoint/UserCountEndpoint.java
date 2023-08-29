package com.viliavin.webflux.endpoint;

import com.viliavin.webflux.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Endpoint(id = "userCount")
public class UserCountEndpoint {

    private final UserRepository repository;

    @ReadOperation
    public Map<String, Long> count() {
        Long totalCount = repository.count().block();
        return Map.of("count", totalCount);
    }

}
