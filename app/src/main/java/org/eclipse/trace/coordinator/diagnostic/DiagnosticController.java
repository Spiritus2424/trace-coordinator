package org.eclipse.trace.coordinator.diagnostic;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.health.Health;
import org.eclipse.tsp.java.client.api.health.HealthStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
@ApplicationScoped
public class DiagnosticController {

    @Inject
    DiagnosticService diagnosticService;

    @Inject
    TraceServerManager traceServerManager;

    @GET
    @Path("health")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHealthStatus() {

        Health healthMerged = this.traceServerManager.getTraceServers()
                .stream()
                .map((TraceServer traceServer) -> this.diagnosticService.getStatus(traceServer))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
                .stream()
                .filter(health -> health.getStatus() == HealthStatus.DOWN)
                .findFirst()
                .isPresent() ? new Health(HealthStatus.DOWN) : new Health(HealthStatus.UP);

        return Response.ok(healthMerged).build();
    }

}
