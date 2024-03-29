package org.eclipse.trace.coordinator.api.experiment;

import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.core.experiment.ExperimentFactory;
import org.eclipse.trace.coordinator.core.experiment.ExperimentService;
import org.eclipse.trace.coordinator.core.trace.TraceService;
import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.trace.coordinator.core.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.experiment.Experiment;
import org.eclipse.tsp.java.client.api.experiment.dto.CreateExperimentRequestDto;
import org.eclipse.tsp.java.client.api.trace.Trace;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.query.Query;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("experiments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExperimentController {

	@Inject
	private ExperimentService experimentService;

	// @Inject
	// DistributedExperimentManager distributedExperimentManager;

	@Inject
	private TraceServerManager traceServerManager;

	@Inject
	private TraceService traceService;

	@PostConstruct
	public void createExperiments() {

		// for (DistributedExperiment distributedExperiment :
		// distributedExperimentManager.getDistributedExperiments()
		// .values()) {
		// for (TraceServer traceServer : traceServerManager.getTraceServers()) {
		// Map<String, Object> parameters = new HashMap<>();
		// parameters.put("name", traceServer.getHost());
		// parameters.put("traces", distributedExperiment.getTracesUuid());
		// experimentService.createExperiment(traceServer, new Query(parameters));
		// }

		// }

	}

	@GET
	public Response getExperiments() {
		final List<Experiment> distributedExperiments = this.traceServerManager.getTraceServers().stream()
				.map(traceSercer -> this.experimentService.getExperiments(traceSercer))
				.map(CompletableFuture::join)
				.flatMap(List::stream)
				.collect(groupingBy(Experiment::getUuid))
				.values()
				.stream()
				.map(ExperimentFactory::createExperiment)
				.collect(Collectors.toList());

		return Response.ok(distributedExperiments).build();
	}

	@GET
	@Path("{expUUID}")
	public Response getExperiment(@NotNull @PathParam("expUUID") final UUID experimentUuid) {
		final List<Experiment> experiments = this.traceServerManager.getTraceServers().stream()
				.map(traceSercer -> this.experimentService.getExperiment(traceSercer, experimentUuid))
				.map(CompletableFuture::join)
				.collect(Collectors.toList());

		Response response = null;
		if (!experiments.isEmpty()) {
			response = Response.ok(ExperimentFactory.createExperiment(experiments)).build();
		} else {
			response = Response.status(Status.NOT_FOUND).entity("No Such Experiment").build();
		}

		return response;
	}

	@POST
	public Response createExperiment(@NotNull @Valid final Body<CreateExperimentRequestDto> body) {
		final List<Experiment> experiments = this.traceServerManager.getTraceServers().stream()
				.map((TraceServer traceServer) -> this.traceService.getTraces(traceServer)
						.thenApply((List<Trace> traces) -> {
							List<UUID> traceServerTracesUuid = traces.stream()
									.filter((Trace trace) -> body.getParameters().getTraces()
											.contains(trace.getUuid()))
									.map(Trace::getUuid)
									.collect(Collectors.toList());

							CreateExperimentRequestDto createExperimentRequestDto = new CreateExperimentRequestDto(
									body.getParameters().getExperimentName(),
									traceServerTracesUuid);

							return this.experimentService.createExperiment(traceServer,
									new Body<>(createExperimentRequestDto));
						}))
				.map(CompletableFuture::join)
				.map(CompletableFuture::join)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		return Response.ok(ExperimentFactory.createExperiment(experiments)).build();
	}

	@PUT
	@Path("{expUUID}")
	public Response updateExperiment(@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@QueryParam("name") String experimentName, @NotNull Query query) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}

	@DELETE
	@Path("{expUUID}")
	public Response deleteExperiment(@NotNull @PathParam("expUUID") final UUID experimentUuid) {
		final List<Experiment> experiments = this.traceServerManager.getTraceServers().stream()
				.map(traceServer -> this.experimentService.deleteExperiment(traceServer, experimentUuid))
				.map(CompletableFuture::join)
				.collect(Collectors.toList());

		Response response = null;
		if (!experiments.isEmpty()) {
			response = Response.ok(ExperimentFactory.createExperiment(experiments)).build();
		} else {
			response = Response.status(Status.NOT_FOUND).entity("No Such Experiment").build();
		}

		return response;
	}
}
