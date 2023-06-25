package org.eclipse.trace.coordinator.api.diagnostic;

import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.core.diagnostic.DiagnosticService;
import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.trace.coordinator.core.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.health.Health;
import org.eclipse.tsp.java.client.api.health.HealthStatus;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
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
				.allMatch(health -> health.getStatus() == HealthStatus.UP)
						? new Health(HealthStatus.UP)
						: new Health(HealthStatus.DOWN);

		return Response.ok(healthMerged).build();
	}

}
