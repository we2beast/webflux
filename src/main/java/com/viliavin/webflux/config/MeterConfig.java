package com.viliavin.webflux.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeterConfig {

    @Bean
    public MeterRegistry meterRegistry() {
        return new CompositeMeterRegistry();
    }

}
