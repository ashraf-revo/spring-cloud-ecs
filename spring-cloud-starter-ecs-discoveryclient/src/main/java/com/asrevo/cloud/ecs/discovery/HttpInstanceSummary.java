package com.asrevo.cloud.ecs.discovery;

import java.util.HashMap;
import java.util.Map;

public class HttpInstanceSummary {
    private Map<String, String> attributes = new HashMap<>();

    public Map<String, String> attributes() {
        return attributes;
    }
}
