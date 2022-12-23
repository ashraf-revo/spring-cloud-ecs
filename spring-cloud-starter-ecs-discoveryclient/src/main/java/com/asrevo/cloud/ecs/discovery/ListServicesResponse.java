package com.asrevo.cloud.ecs.discovery;

import java.util.List;

public class ListServicesResponse {
    public List<ServiceSummary> services() {
        return List.of(new ServiceSummary());
    }
}
