package com.asrevo.cloud.ecs.discovery;

import com.amazonaws.services.servicediscovery.AWSServiceDiscoveryAsync;
import com.amazonaws.services.servicediscovery.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class EcsReactiveDiscoveryClient implements ReactiveDiscoveryClient {
    private final EcsDiscoveryProperties properties;
    private final AWSServiceDiscoveryAsync discoveryAsync;

    public EcsReactiveDiscoveryClient(EcsDiscoveryProperties properties, AWSServiceDiscoveryAsync discoveryAsync) {
        this.properties = properties;
        this.discoveryAsync = discoveryAsync;
    }

    @Override
    public String description() {
        return "ecs reactive discovery client";
    }

    @Override
    public Flux<ServiceInstance> getInstances(String serviceId) {

        return Flux.defer(() -> Flux.fromIterable(getDefaultServiceInstances(this.discoveryAsync, this.properties,
                serviceId))).subscribeOn(Schedulers.boundedElastic());
    }

    public static List<ServiceInstance> getDefaultServiceInstances(AWSServiceDiscoveryAsync discoveryAsync,
                                                                   EcsDiscoveryProperties properties,
                                                                   String serviceId) {
        DiscoverInstancesRequest request = new DiscoverInstancesRequest();
        request.setNamespaceName(properties.getNamespace());
        request.setServiceName(serviceId);
        List<HttpInstanceSummary> instances = discoveryAsync.discoverInstances(request).getInstances();
        log.info("getting " + instances.size() + " services for " + serviceId);
        return instances.stream().map(CloudMapServiceInstance::new).collect(Collectors.toList());
    }

    @Override
    public Flux<String> getServices() {
        return Flux.defer(() -> Flux.fromIterable(getEcsServices(this.discoveryAsync))).subscribeOn(Schedulers.boundedElastic());
    }

    public static List<String> getEcsServices(AWSServiceDiscoveryAsync discoveryAsync) {
        ListServicesRequest listServicesRequest = new ListServicesRequest();
        ListServicesResult listServicesResult = discoveryAsync.listServices(listServicesRequest);
        log.info("getting all services");
        List<String> discoveredServices =
                listServicesResult.getServices().stream().map(ServiceSummary::getName).collect(Collectors.toList());
        log.info("got those services " + discoveredServices);
        return discoveredServices;
    }
}

