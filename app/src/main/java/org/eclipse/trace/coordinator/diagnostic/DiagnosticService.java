package org.eclipse.trace.coordinator.diagnostic;

import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.health.Health;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DiagnosticService {

    public CompletableFuture<Health> getStatus(final TraceServer traceServer) {
        return traceServer.getTspClient().getHealthApiAsync().checkHealth()
                .thenApply(response -> response.getResponseModel());
    }
}
