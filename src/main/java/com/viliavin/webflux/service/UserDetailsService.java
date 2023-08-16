package com.viliavin.webflux.service;

import com.viliavin.webflux.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements ReactiveUserDetailsService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository repository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        logger.debug("username: " + username);
        return repository.findByUsername(username).map(userEntity ->
            User.withUsername(userEntity.username())
                .password(userEntity.password())
                .roles(userEntity.role().name()).build()
        );
    }

}
