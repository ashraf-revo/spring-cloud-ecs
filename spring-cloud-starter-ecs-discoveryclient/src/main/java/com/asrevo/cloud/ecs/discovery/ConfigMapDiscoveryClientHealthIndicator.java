package com.asrevo.cloud.ecs.discovery;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.context.ApplicationEventPublisher;

public class ConfigMapDiscoveryClientHealthIndicator implements InitializingBean {
    private final ApplicationEventPublisher applicationEventPublisher;

    public ConfigMapDiscoveryClientHealthIndicator(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        applicationEventPublisher
                .publishEvent(new InstanceRegisteredEvent<>("current", null));
    }
}
