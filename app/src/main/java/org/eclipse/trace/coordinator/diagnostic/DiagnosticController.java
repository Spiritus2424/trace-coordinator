package org.eclipse.trace.coordinator.diagnostic;

import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.health.Health;
import org.eclipse.tsp.java.client.api.health.HealthStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DiagnosticController {

    @Inject
    DiagnosticService diagnosticService;

    @Inject
    TraceServerManager traceServerManager;

    @GET
    @Path("health")
    public Response getHealthStatus() {
        final Health healthMerged = this.traceServerManager.getTraceServers()
                .stream()
                .map((TraceServer traceServer) -> this.diagnosticService.getStatus(traceServer))
                .map(CompletableFuture::join)
                .filter(health -> health.getStatus() == HealthStatus.DOWN)
                .findFirst()
                .isPresent() ? new Health(HealthStatus.DOWN) : new Health(HealthStatus.UP);

        return Response.ok(healthMerged).build();
    }

}
