package com.asrevo.cloud.ecs.discovery;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties("spring.cloud.ecs.config")
public class EcsConfigProperties {
    private boolean enabled = true;
    private List<String> sources=new ArrayList<>();
    private String path = "/";
    private String ssmRegion;
}
