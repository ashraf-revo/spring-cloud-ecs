package com.asrevo.cloud.ecs.discovery;

import com.amazonaws.services.servicediscovery.model.HttpInstanceSummary;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;

import java.net.URI;
import java.util.Map;

public class CloudMapServiceInstance implements ServiceInstance {
    private final HttpInstanceSummary instance;

    public CloudMapServiceInstance(HttpInstanceSummary instance) {
        this.instance = instance;
    }


    @Override
    public String getServiceId() {
        return instance.getServiceName();
    }

    public String getNamespace() {
        return instance.getNamespaceName();
    }

    @Override
    public String getHost() {
        return instance.getAttributes().get("AWS_INSTANCE_IPV4");
    }

    @Override
    public int getPort() {
        return Integer.parseInt(instance.getAttributes().get("AWS_INSTANCE_PORT"));
    }

    @Override
    public boolean isSecure() {
        return Boolean.parseBoolean(instance.getAttributes().get("SECURE"));
    }

    @Override
    public URI getUri() {
        return DefaultServiceInstance.getUri(this);
    }

    @Override
    public Map<String, String> getMetadata() {
        return instance.getAttributes();
    }
}
