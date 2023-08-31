package org.eclipse.trace.coordinator.api.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.core.configuration.ConfigurationProvider;
import org.eclipse.trace.coordinator.core.trace.TraceService;
import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.trace.coordinator.core.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.trace.Trace;
import org.eclipse.tsp.java.client.api.trace.dto.OpenTraceRequestDto;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.glassfish.hk2.api.Immediate;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("traces")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Immediate
public class TraceController {
	private static final String NO_SUCH_TRACE = "No Such Trace";

	@Inject
	private ConfigurationProvider configurationProvider;

	@Inject
	private TraceService traceService;

	@Inject
	private TraceServerManager traceServerManager;

	@PostConstruct
	public void loadTraces() {
		Logger.getLogger(TraceController.class.getName()).log(Level.INFO, "PostContruct Lauch");
		final List<CompletableFuture<Void>> futureList = new ArrayList<>();
		final String scheme = "(.*)";
		for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
			List<String> tracesPath = this.configurationProvider.getConfiguration().getTraceServerProperties().stream()
					.filter(properties -> properties.getHost().matches(scheme.concat(traceServer.getHost()))
							&& properties.getPort().matches(traceServer.getPort()))
					.findFirst().get()
					.getTracesPath();
			for (String tracePath : tracesPath) {
				OpenTraceRequestDto openTraceRequestDto = new OpenTraceRequestDto(tracePath);
				openTraceRequestDto.setMaxDepth(3);
				openTraceRequestDto.setName(traceServer.getHost().concat("$"));

				futureList.add(
						this.traceService.openTraces(traceServer, new Body<>(openTraceRequestDto))
								.thenAccept(traces -> {
									traceServer.getTraces().addAll(traces);
								}));
			}
		}

		CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).join();

	}

	@GET
	public Response getTraces() {
		final List<Trace> traces = this.traceServerManager.getTraceServers().stream()
				.map((TraceServer traceServer) -> this.traceService.getTraces(traceServer))
				.map(CompletableFuture::join)
				.flatMap(List::stream)
				.collect(Collectors.toList());

		return Response.ok(traces).build();
	}

	@GET
	@Path("{uuid}")
	public Response getTrace(@NotNull @PathParam("uuid") final UUID traceUuid) {
		final Optional<Trace> trace = this.traceServerManager.getTraceServers().stream()
				.map((TraceServer traceServer) -> this.traceService.getTrace(traceServer, traceUuid))
				.map(CompletableFuture::join)
				.findFirst();
		Response response = null;

		if (trace.isPresent()) {
			response = Response.ok(trace.get()).build();
		} else {
			response = Response.status(Status.NOT_FOUND).entity(NO_SUCH_TRACE).build();
		}

		return response;
	}

	@POST
	public Response openTrace(@NotNull @Valid final Body<OpenTraceRequestDto> body) {
		final List<Trace> traces = this.traceServerManager.getTraceServers().stream()
				.map((TraceServer traceServer) -> {
					String traceName = body.getParameters().getName();
					if (body.getParameters().getMaxDepth() == 0) {
						if (traceName == null) {
							final String[] uriSplit = body.getParameters().getUri().split("/");
							traceName = uriSplit[uriSplit.length - 1];
						}
						traceName = String.format("%s$%s", traceServer.getHost(), traceName.replace("/", "\\"));
					} else {
						traceName = String.format("%s$%s", traceServer.getHost(), body.getParameters().getName());
					}

					body.getParameters()
							.setName(traceName);
					return this.traceService.openTraces(traceServer, body);
				})
				.map(CompletableFuture::join)
				.flatMap(List::stream)
				.collect(Collectors.toList());

		Response response = null;
		if (!traces.isEmpty()) {
			response = Response.ok(traces).build();
		} else {
			response = Response.status(Status.NOT_FOUND).entity(NO_SUCH_TRACE).build();
		}

		return response;
	}

	@DELETE
	@Path("{uuid}")
	public Response deleteTrace(@NotNull @PathParam("uuid") final UUID traceUuid) {
		final Optional<Trace> trace = this.traceServerManager.getTraceServers().stream()
				.map((TraceServer traceServer) -> this.traceService.deleteTrace(traceServer, traceUuid))
				.filter(Objects::nonNull)
				.map(CompletableFuture::join)
				.findFirst();

		Response response = null;
		if (trace.isPresent()) {
			response = Response.ok(trace.get()).build();
		} else {
			response = Response.status(Status.NOT_FOUND).entity(NO_SUCH_TRACE).build();
		}

		return response;
	}

}
