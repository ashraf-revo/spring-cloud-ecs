package com.asrevo.cloud.ecs.discovery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.servicediscovery.ServiceDiscoveryAsyncClient;
import software.amazon.awssdk.services.servicediscovery.model.*;

@Slf4j
public class EcsReactiveDiscoveryClient implements ReactiveDiscoveryClient {
    private final EcsDiscoveryProperties properties;
    private final ServiceDiscoveryAsyncClient discoveryAsync;

    public EcsReactiveDiscoveryClient(EcsDiscoveryProperties properties, ServiceDiscoveryAsyncClient discoveryAsync) {
        this.properties = properties;
        this.discoveryAsync = discoveryAsync;
    }

    @Override
    public String description() {
        return "ecs reactive discovery client";
    }

    @Override
    public Flux<ServiceInstance> getInstances(String serviceId) {
        return getDefaultServiceInstances(this.discoveryAsync, this.properties, serviceId);
    }

    public static Flux<ServiceInstance> getDefaultServiceInstances(ServiceDiscoveryAsyncClient discoveryAsync,
                                                                   EcsDiscoveryProperties properties,
                                                                   String serviceId) {
        DiscoverInstancesRequest request = DiscoverInstancesRequest.builder()
                .namespaceName(properties.getNamespace())
                .serviceName(serviceId)
                .build();
        return Mono.fromFuture(discoveryAsync.discoverInstances(request)).map(DiscoverInstancesResponse::instances)
                .doOnEach(it -> {
                    if (it.hasValue()) {
                        log.info("getting " + it.get().size() + " services for " + serviceId);
                    }
                })
                .flatMapMany(Flux::fromIterable)
                .map(instanceSummary -> {
                    String host = instanceSummary.attributes().get("AWS_INSTANCE_IPV4");
                    int port = Integer.parseInt(instanceSummary.attributes().get("AWS_INSTANCE_PORT"));
                    boolean secure = Boolean.parseBoolean(instanceSummary.attributes().get("SECURE"));
                    return new DefaultServiceInstance(instanceSummary.instanceId(), serviceId, host, port, secure,
                            instanceSummary.attributes());
                });
    }

    @Override
    public Flux<String> getServices() {
        return getEcsServices(this.discoveryAsync);
    }

    public static Flux<String> getEcsServices(ServiceDiscoveryAsyncClient discoveryAsync) {
        ListServicesRequest listServicesRequest = ListServicesRequest.builder().build();
        log.info("getting all services");
        return Mono.fromFuture(discoveryAsync.listServices(listServicesRequest)).map(ListServicesResponse::services)
                .flatMapMany(Flux::fromIterable)
                .map(ServiceSummary::name);
    }
}

