package org.eclipse.trace.coordinator.api.timegraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.core.action.ActionManager;
import org.eclipse.trace.coordinator.core.timegraph.CriticalPathAction;
import org.eclipse.trace.coordinator.core.timegraph.TimeGraphService;
import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.trace.coordinator.core.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphArrow;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphEntry;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphModel;
import org.eclipse.tsp.java.client.api.timegraph.dto.GetTimeGraphArrowsRequestDto;
import org.eclipse.tsp.java.client.api.timegraph.dto.GetTimeGraphStatesRequestDto;
import org.eclipse.tsp.java.client.api.timegraph.dto.GetTimeGraphTooltipsRequestDto;
import org.eclipse.tsp.java.client.api.timegraph.dto.GetTimeGraphTreeRequestDto;
import org.eclipse.tsp.java.client.core.action.ActionDescriptor;
import org.eclipse.tsp.java.client.shared.entry.EntryHeader;
import org.eclipse.tsp.java.client.shared.entry.EntryModel;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.eclipse.tsp.java.client.shared.response.ResponseStatus;

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
import jakarta.ws.rs.core.Response.Status;

@Path("experiments/{expUUID}/outputs/timeGraph/{outputId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TimeGraphController {

	@Inject
	private TimeGraphService timeGraphService;

	@Inject
	private TraceServerManager traceServerManager;

	@Inject
	private ActionManager actionManager;

	@POST
	@Path("arrows")
	public Response getArrows(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Body<GetTimeGraphArrowsRequestDto> body) {
		final GenericResponse<List<TimeGraphArrow>> genericResponseMerged = this.traceServerManager.getTraceServers()
				.stream()
				.map((TraceServer traceServer) -> this.timeGraphService.getArrows(traceServer, experimentUuid, outputId,
						body))
				.map(CompletableFuture::join)
				.reduce(null, (accumulator, genericResponse) -> {
					if (accumulator == null) {
						accumulator = genericResponse;
					} else {
						if (accumulator.getStatus() != ResponseStatus.RUNNING) {
							accumulator.setStatus(genericResponse.getStatus());
							accumulator.setMessage(genericResponse.getMessage());
						}
						if (genericResponse.getModel() != null) {
							accumulator.getModel().addAll(genericResponse.getModel());
						}
					}

					return accumulator;
				});

		return Response.ok(genericResponseMerged).build();
	}

	@POST
	@Path("states")
	public Response getStates(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Body<GetTimeGraphStatesRequestDto> body) {
		final GenericResponse<TimeGraphModel> genericResponseMerged = this.traceServerManager.getTraceServers()
				.stream()
				.map((TraceServer traceServer) -> this.timeGraphService.getStates(traceServer, experimentUuid, outputId,
						body))
				.filter(Objects::nonNull)
				.map(CompletableFuture::join)
				.reduce(null, (accumulator, genericResponse) -> {
					if (accumulator == null) {
						accumulator = genericResponse;
					} else {
						if (accumulator.getStatus() != ResponseStatus.RUNNING) {
							accumulator.setStatus(genericResponse.getStatus());
							accumulator.setMessage(genericResponse.getMessage());
						}
						if (genericResponse.getModel() != null) {
							accumulator.getModel().getRows().addAll(genericResponse.getModel().getRows());
						}
					}

					return accumulator;
				});

		return Response.ok(genericResponseMerged).build();
	}

	@POST
	@Path("tooltip")
	public Response getTooltips(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Body<GetTimeGraphTooltipsRequestDto> body) {
		final GenericResponse<Map<String, String>> genericResponseMerged = this.traceServerManager.getTraceServers()
				.stream()
				.map((TraceServer traceServer) -> this.timeGraphService.getTooltips(traceServer, experimentUuid,
						outputId,
						body))
				.filter(Objects::nonNull)
				.map(CompletableFuture::join)
				.reduce(null, (accumulator, genericResponse) -> {
					if (accumulator == null) {
						accumulator = genericResponse;
					} else {
						if (accumulator.getStatus() != ResponseStatus.RUNNING) {
							accumulator.setStatus(genericResponse.getStatus());
							accumulator.setMessage(genericResponse.getMessage());
						}
						if (genericResponse.getModel() != null) {
							accumulator.getModel().putAll(genericResponse.getModel());
						}
					}

					return accumulator;
				});

		return Response.ok(genericResponseMerged).build();
	}

	@POST
	@Path("tree")
	public Response getTree(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Body<GetTimeGraphTreeRequestDto> body) {
		final Set<EntryHeader> headers = new HashSet<>();
		final GenericResponse<EntryModel<TimeGraphEntry>> genericResponseMerged = this.traceServerManager
				.getTraceServers()
				.stream()
				.map((TraceServer traceServer) -> this.timeGraphService.getTree(traceServer, experimentUuid, outputId,
						body))
				.map(CompletableFuture::join)
				.reduce(null, (accumulator, genericResponse) -> {
					if (accumulator == null) {
						accumulator = genericResponse;
					} else {
						if (accumulator.getStatus() != ResponseStatus.RUNNING) {
							accumulator.setStatus(genericResponse.getStatus());
							accumulator.setMessage(genericResponse.getMessage());
						}
						if (genericResponse.getModel() != null) {
							accumulator.getModel().getEntries().addAll(genericResponse.getModel().getEntries());
							headers.addAll(genericResponse.getModel().getHeaders());
						}
					}
					return accumulator;
				});

		if (genericResponseMerged.getModel() != null) {
			genericResponseMerged.getModel().setHeaders(new ArrayList<>(headers));
		}

		return Response.ok(genericResponseMerged).build();
	}

	@POST
	@Path("navigate/states")
	public Response getNavigations(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Query query) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}

	@POST
	@Path("tooltip/actions")
	public Response getActionTooltips(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Body<GetTimeGraphTooltipsRequestDto> body) {
		final GenericResponse<List<ActionDescriptor>> genericResponseMerged = this.traceServerManager.getTraceServers()
				.stream()
				.map((TraceServer traceServer) -> this.timeGraphService.getActionTooltips(traceServer, experimentUuid,
						outputId,
						body))
				.filter(Objects::nonNull)
				.map(CompletableFuture::join)
				.reduce(null, (accumulator, genericResponse) -> {
					if (accumulator == null) {
						accumulator = genericResponse;
					} else {
						if (accumulator.getStatus() != ResponseStatus.RUNNING) {
							accumulator.setStatus(genericResponse.getStatus());
							accumulator.setMessage(genericResponse.getMessage());
						}
						if (genericResponse.getModel() != null) {
							accumulator.getModel().addAll(genericResponse.getModel());
						}
					}

					return accumulator;
				});

		return Response.ok(genericResponseMerged).build();
	}

	@POST
	@Path("tooltip/actions/{actionId}")
	public Response applyActionTooltips(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @PathParam("actionId") final String actionId,
			@NotNull @Valid final Body<Map<String, Object>> body) {
		this.traceServerManager.getTraceServers()
				.stream()
				.forEach((TraceServer traceServer) -> {
					this.timeGraphService.applyActionTooltip(traceServer,
							experimentUuid,
							outputId,
							actionId,
							body).whenComplete((Void, exception) -> {
								if (exception == null) {
									this.actionManager.getActionApplied().put(experimentUuid,
											new CriticalPathAction(traceServer, experimentUuid));
								}
							});
				});
		return Response.ok().build();
	}
}
