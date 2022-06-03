package com.asrevo.cloud.ecs.discovery;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.servicediscovery.AWSServiceDiscoveryAsync;
import com.amazonaws.services.servicediscovery.AWSServiceDiscoveryAsyncClientBuilder;
import io.awspring.cloud.core.region.RegionProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnDiscoveryHealthIndicatorEnabled;
import org.springframework.cloud.client.discovery.health.DiscoveryClientHealthIndicatorProperties;
import org.springframework.cloud.client.discovery.health.reactive.ReactiveDiscoveryClientHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class EcsConfig {

    @ConditionalOnProperty(value = "spring.cloud.ecs.discovery.enabled", matchIfMissing = true)
    @Configuration
    public static class EcsReactiveDiscoveryClientAutoConfiguration {

        @Autowired
        private DefaultAWSCredentialsProviderChain awsCredentialsProvider;
        @Autowired
        private RegionProvider regionProvider;

        @Bean
        public AWSServiceDiscoveryAsync awsServiceDiscoveryAsync() {
            return AWSServiceDiscoveryAsyncClientBuilder.standard().withCredentials(awsCredentialsProvider).withRegion(regionProvider.getRegion().getName()).build();
        }

        @Bean
        @ConditionalOnMissingBean
        public EcsReactiveDiscoveryClient reactiveDiscoveryClient(EcsDiscoveryProperties ecsDiscoveryProperties,
                                                                  AWSServiceDiscoveryAsync discoveryAsync) {
            return new EcsReactiveDiscoveryClient(ecsDiscoveryProperties, discoveryAsync);
        }

        @Bean
        @ConditionalOnClass(name = "org.springframework.boot.actuate.health.ReactiveHealthIndicator")
        @ConditionalOnDiscoveryHealthIndicatorEnabled
        public ReactiveDiscoveryClientHealthIndicator ecsReactiveDiscoveryClientHealthIndicator(EcsReactiveDiscoveryClient client, DiscoveryClientHealthIndicatorProperties properties) {
            return new ReactiveDiscoveryClientHealthIndicator(client, properties);
        }
    }

}
