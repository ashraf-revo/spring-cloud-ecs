package com.asrevo.cloud.ecs.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EcsConfigClientBootstrapConfiguration {
    @Bean
    @ConditionalOnProperty(value = "spring.cloud.ecs.config.enabled", matchIfMissing = true)
    public EcsConfigClientPropertySourceLocator configMapPropertySourceLocator(EcsConfigProperties properties) {
        return new EcsConfigClientPropertySourceLocator(properties);
    }
}
