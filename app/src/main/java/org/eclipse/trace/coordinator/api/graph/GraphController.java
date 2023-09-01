package org.eclipse.trace.coordinator.api.graph;

import java.util.UUID;

import org.eclipse.trace.coordinator.core.action.ActionManager;
import org.eclipse.trace.coordinator.core.graph.GraphService;
import org.eclipse.trace.coordinator.core.timegraph.CriticalPathAction;
import org.eclipse.trace.coordinator.core.timegraph.TimeGraphService;
import org.eclipse.trace.coordinator.core.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphModel;
import org.eclipse.tsp.java.client.api.timegraph.dto.GetTimeGraphStatesRequestDto;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.glassfish.hk2.api.ServiceLocator;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("experiments/{expUUID}/graph")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GraphController {
	@Inject
	private TimeGraphService timeGraphService;

	@Inject
	private GraphService graphService;
	@Inject
	private TraceServerManager traceServerManager;

	@Inject
	private ServiceLocator serviceLocator;

	@Inject
	private ActionManager actionManager;

	@POST
	@Path("criticalpath")
	public Response getCriticalPath(@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @Valid final Body<GetTimeGraphStatesRequestDto> body) {
		CriticalPathAction criticalPathAction = this.actionManager.getActionApplied().get(experimentUuid).stream()
				.filter(CriticalPathAction.class::isInstance)
				.map(CriticalPathAction.class::cast)
				.findFirst().get();
		this.serviceLocator.inject(criticalPathAction);
		String outputId = "org.eclipse.tracecompass.analysis.graph.core.dataprovider.CriticalPathDataProvider";
		GenericResponse<TimeGraphModel> genericResponse = this.timeGraphService
				.getStates(criticalPathAction.getHostTraceServer(), experimentUuid, outputId, body)
				.join();
		criticalPathAction.compute(genericResponse.getModel());
		return Response.ok(genericResponse).build();
	}
}
