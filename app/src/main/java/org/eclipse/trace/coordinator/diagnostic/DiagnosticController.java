package org.eclipse.trace.coordinator.diagnostic;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.models.health.Health;
import org.eclipse.tsp.java.client.models.health.HealthStatus;

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
        Health health = null;
        for (TraceServer traceServer : traceServerManager.getTraceServers()) {
            health = diagnosticService.getStatus(traceServer);
            if (health.getStatus() == HealthStatus.DOWN) {
                break;
            }
        }

        return Response.ok(health).build();
    }
}
