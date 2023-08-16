package com.viliavin.webflux.config;

import com.viliavin.webflux.domain.entity.Role;
import com.viliavin.webflux.domain.entity.UserEntity;
import com.viliavin.webflux.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class InitDefaultUsersConfig implements InitializingBean {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository repository;

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Create a default users...");
        List<UserEntity> userEntityList = List.of(
            new UserEntity("userId" , "user", bCryptPasswordEncoder().encode("password"), Role.USER),
            new UserEntity("adminId" , "admin", bCryptPasswordEncoder().encode("password"), Role.ADMIN)
        );

        repository.saveAll(userEntityList).blockLast();
        logger.info("Finish create a default users");
    }
}
