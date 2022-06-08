package com.asrevo.cloud.ecs.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(EcsConfigProperties.PREFIX)
@Getter
@Setter
public class EcsConfigProperties {
    public static final String PREFIX = "spring.cloud.ecs.config";
    private boolean enabled = true;
    private List<String> paths = new ArrayList<>();
    private String region;

}
