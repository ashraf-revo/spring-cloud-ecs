package com.asrevo.cloud.ecs.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.awspring.cloud.core.region.RegionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EcsConfigClientBootstrapConfiguration {




    @Bean
    @ConditionalOnProperty(value = "spring.cloud.ecs.config.enabled", matchIfMissing = true)
    public EcsConfigClientPropertySourceLocator configMapPropertySourceLocator(EcsConfigProperties properties
                                                                               ) {
        return new EcsConfigClientPropertySourceLocator(properties);
    }
}
