package com.asrevo.cloud.ecs.discovery;

import lombok.Builder;

@Builder
public class DiscoverInstancesRequest {
    private String namespaceName;
    private String serviceName;

    public String namespaceName() {
        return namespaceName;
    }
}
