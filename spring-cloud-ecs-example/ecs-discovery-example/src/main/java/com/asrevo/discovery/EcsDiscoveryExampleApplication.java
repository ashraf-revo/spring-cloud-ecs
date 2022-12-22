package com.asrevo.discovery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;

@SpringBootApplication
public class EcsDiscoveryExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcsDiscoveryExampleApplication.class, args);
    }

    @Bean
    public AwsRegionProvider regionProvider(@Value("${spring.cloud.aws.region.static}") String region) {
        return () -> Region.of(region);
    }
}
