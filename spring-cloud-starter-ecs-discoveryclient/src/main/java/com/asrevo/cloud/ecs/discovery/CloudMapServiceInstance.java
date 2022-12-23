package com.asrevo.cloud.ecs.discovery;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
//import software.amazon.awssdk.services.servicediscovery.model.HttpInstanceSummary;

import java.net.URI;
import java.util.Map;

public class CloudMapServiceInstance implements ServiceInstance {
    private final HttpInstanceSummary instance;
    private final String serviceId;
    private final String namespace;
    public CloudMapServiceInstance(String serviceId,String namespace,HttpInstanceSummary instance) {
        this.instance = instance;
        this.serviceId = serviceId;
        this.namespace = namespace;
    }


    @Override
    public String getServiceId() {
        return this.serviceId;
    }

    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public String getHost() {
        return instance.attributes().get("AWS_INSTANCE_IPV4");
    }

    @Override
    public int getPort() {
        return Integer.parseInt(instance.attributes().get("AWS_INSTANCE_PORT"));
    }

    @Override
    public boolean isSecure() {
        return Boolean.parseBoolean(instance.attributes().get("SECURE"));
    }

    @Override
    public URI getUri() {
        return DefaultServiceInstance.getUri(this);
    }

    @Override
    public Map<String, String> getMetadata() {
        return instance.attributes();
    }
}
