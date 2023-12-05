package org.eclipse.trace.coordinator.api.timegraph;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
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
import org.eclipse.tsp.java.client.shared.entry.EntryModel;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.eclipse.tsp.java.client.shared.response.ResponseStatus;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLog;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLogUtils.FlowScopeLog;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLogUtils.FlowScopeLogBuilder;
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
import jakarta.ws.rs.core.Response.Status;

@Path("experiments/{expUUID}/outputs/timeGraph/{outputId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TimeGraphController {

	private final @NonNull Logger logger;

	@Inject
	private TimeGraphService timeGraphService;

	@Inject
	private TraceServerManager traceServerManager;

	@Inject
	private ActionManager actionManager;

	public TimeGraphController() {
		this.logger = TraceCompassLog.getLogger(TimeGraphController.class);

	}

	@POST
	@Path("arrows")
	public Response getArrows(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Body<GetTimeGraphArrowsRequestDto> body) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"TimeGraphController#getArrows").build()) {
			final GenericResponse<List<TimeGraphArrow>> genericResponseMerged = this.traceServerManager
					.getTraceServers()
					.stream()
					.map((TraceServer traceServer) -> this.timeGraphService.getArrows(traceServer, experimentUuid,
							outputId,
							body))
					.map(CompletableFuture::join)
					.reduce(null, (accumulator, genericResponse) -> {
						if (accumulator == null) {
							accumulator = genericResponse;
						} else if (genericResponse != null) {
							if (accumulator.getStatus() != ResponseStatus.RUNNING) {
								accumulator.setStatus(genericResponse.getStatus());
								accumulator.setMessage(genericResponse.getMessage());
							}
							if (genericResponse.getModel() != null) {
								accumulator.setModel(
										Stream.concat(accumulator.getModel().parallelStream(),
												genericResponse.getModel().parallelStream())
												.collect(Collectors.toList()));
							}
						}

						return accumulator;
					});

			return Response.ok(genericResponseMerged).build();
		}
	}

	@POST
	@Path("states")
	public Response getStates(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Body<GetTimeGraphStatesRequestDto> body) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"TimeGraphController#getStates").build()) {
			final GenericResponse<TimeGraphModel> genericResponseMerged = this.traceServerManager.getTraceServers()
					.stream()
					.map((TraceServer traceServer) -> this.timeGraphService.getStates(traceServer, experimentUuid,
							outputId,
							body))
					.filter(Objects::nonNull)
					.map(CompletableFuture::join)
					.reduce(null, (accumulator, genericResponse) -> {
						if (accumulator == null) {
							accumulator = genericResponse;
						} else if (genericResponse != null) {
							if (accumulator.getStatus() != ResponseStatus.RUNNING) {
								accumulator.setStatus(genericResponse.getStatus());
								accumulator.setMessage(genericResponse.getMessage());
							}
							if (genericResponse.getModel() != null) {
								accumulator.getModel().setRows(
										Stream.concat(accumulator.getModel().getRows().parallelStream(),
												genericResponse.getModel().getRows().parallelStream())
												.collect(Collectors.toList()));
							}
						}

						return accumulator;
					});

			return Response.ok(genericResponseMerged).build();
		}
	}

	@POST
	@Path("tooltip")
	public Response getTooltips(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Body<GetTimeGraphTooltipsRequestDto> body) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"TimeGraphController#getTooltips").build()) {

			final GenericResponse<Map<String, String>> genericResponseMerged = this.traceServerManager.getTraceServers()
					.parallelStream()
					.map((TraceServer traceServer) -> this.timeGraphService.getTooltips(traceServer, experimentUuid,
							outputId,
							body))
					.filter(Objects::nonNull)
					.map(CompletableFuture::join)
					.reduce(null, (accumulator, genericResponse) -> {
						if (accumulator == null) {
							accumulator = genericResponse;
						} else if (genericResponse != null) {
							if (accumulator.getStatus() != ResponseStatus.RUNNING) {
								accumulator.setStatus(genericResponse.getStatus());
								accumulator.setMessage(genericResponse.getMessage());
							}
							if (genericResponse.getModel() != null) {
								accumulator.setModel(
										Stream.concat(accumulator.getModel().entrySet().parallelStream(),
												genericResponse.getModel().entrySet().parallelStream())
												.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
							}
						}

						return accumulator;
					});

			return Response.ok(genericResponseMerged).build();
		}
	}

	@POST
	@Path("tree")
	public Response getTree(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Body<GetTimeGraphTreeRequestDto> body) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"TimeGraphController#getTree").build()) {

			final GenericResponse<EntryModel<TimeGraphEntry>> genericResponseMerged = this.traceServerManager
					.getTraceServers()
					.parallelStream()
					.map((TraceServer traceServer) -> this.timeGraphService.getTree(traceServer, experimentUuid,
							outputId,
							body))
					.map(CompletableFuture::join)
					.filter(Objects::nonNull)
					.reduce(null, (accumulator, genericResponse) -> {
						if (accumulator == null) {
							accumulator = genericResponse;
						} else if (genericResponse != null) {
							if (accumulator.getStatus() != ResponseStatus.RUNNING) {
								accumulator.setStatus(genericResponse.getStatus());
								accumulator.setMessage(genericResponse.getMessage());
							}
							if (genericResponse.getModel() != null) {
								accumulator.getModel().setEntries(
										Stream.concat(accumulator.getModel().getEntries().parallelStream(),
												genericResponse.getModel().getEntries().parallelStream())
												.collect(Collectors.toList()));

								accumulator.getModel().setHeaders(Stream
										.concat(accumulator.getModel().getHeaders().parallelStream(),
												genericResponse.getModel().getHeaders().parallelStream())
										.distinct()
										.collect(Collectors.toList()));
							}
						}
						return accumulator;
					});

			return Response.ok(genericResponseMerged).build();
		}
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
					} else if (genericResponse != null) {
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

	@Inject
	ServiceLocator serviceLocator;

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
											new CriticalPathAction(traceServer,
													experimentUuid));
								}
							});
				});
		return Response.ok().build();
	}
}
