package org.eclipse.trace.coordinator.api.diagnostic;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.trace.coordinator.core.diagnostic.DiagnosticService;
import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.trace.coordinator.core.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.health.Health;
import org.eclipse.tsp.java.client.api.health.HealthStatus;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLog;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLogUtils.FlowScopeLog;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLogUtils.FlowScopeLogBuilder;
import org.glassfish.hk2.api.Immediate;

import jakarta.annotation.PostConstruct;
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
@Immediate
public class DiagnosticController {
	private final @NonNull Logger logger;

	@Inject
	DiagnosticService diagnosticService;

	@Inject
	TraceServerManager traceServerManager;

	public DiagnosticController() {
		this.logger = TraceCompassLog.getLogger(DiagnosticController.class);

	}

	@PostConstruct
	private void checkTraceServerStatus() {
		Logger.getLogger(DiagnosticController.class.getName()).log(Level.INFO, "Tracer Server Status");
		List<Health> healths = traceServerManager.getTraceServers().stream()
				.map(traceServer -> this.diagnosticService.getStatus(traceServer))
				.map(CompletableFuture::join)
				.collect(Collectors.toList());

		if (healths.stream().allMatch(health -> health.getStatus() == HealthStatus.DOWN)) {
			Logger.getLogger(DiagnosticController.class.getName()).log(Level.SEVERE, "All trace-servers are down");
		} else if (healths.stream().allMatch(health -> health.getStatus() == HealthStatus.UP)) {
			Logger.getLogger(DiagnosticController.class.getName()).log(Level.INFO, "All trace-servers are up");
		} else {
			for (int i = 0; i < healths.size(); i++) {
				String traceServerUri = this.traceServerManager.getTraceServers().get(i).getUri().toString();
				HealthStatus healthStatus = healths.get(i).getStatus();
				Level levelType = (healthStatus == HealthStatus.UP) ? Level.FINE : Level.WARNING;
				Logger.getLogger(DiagnosticController.class.getName()).log(levelType,
						String.format("%s : %s", traceServerUri, healthStatus.name()));

			}
		}
	}

	@GET
	@Path("health")
	public Response getHealthStatus() {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"DiagnosticController#getHealthStatus").build()) {

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

}
