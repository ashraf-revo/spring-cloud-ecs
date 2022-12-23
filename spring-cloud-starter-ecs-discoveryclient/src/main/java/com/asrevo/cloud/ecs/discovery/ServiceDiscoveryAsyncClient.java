package com.asrevo.cloud.ecs.discovery;

//import software.amazon.awssdk.services.servicediscovery.model.ListServicesRequest;

import java.util.concurrent.CompletableFuture;

public class ServiceDiscoveryAsyncClient {
    public CompletableFuture<DiscoverInstancesResponse> discoverInstances(DiscoverInstancesRequest request) {
        return CompletableFuture.completedFuture(new DiscoverInstancesResponse());
    }

    public CompletableFuture<ListServicesResponse> listServices(ListServicesRequest request) {
        return CompletableFuture.completedFuture(new ListServicesResponse());
    }
}
